package ru.iquack.linkpreview.core

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import ru.iquack.linkpreview.core.memory.LinkPreviewCache
import ru.iquack.linkpreview.core.request.CachePolicy
import ru.iquack.linkpreview.core.request.LinkPreviewRequest
import ru.iquack.linkpreview.core.request.LinkPreviewResult
import ru.iquack.linkpreview.core.request.LinkPreviewResultError
import ru.iquack.linkpreview.core.request.LinkPreviewResultSuccess

internal class RealLinkPreviewLoader(
    val options: Options
): LinkPreviewLoader {

    override val memoryCache: LinkPreviewCache by options.memoryCache
    override val memoryCachePolicy: CachePolicy get() = options.memoryCachePolicy

    override suspend fun execute(request: LinkPreviewRequest): LinkPreviewResult {
        val cachePolicy = request.memoryCachePolicy?: memoryCachePolicy
        val cacheKey = request.memoryCacheKey?: request.url
        try {
            return withContext(Dispatchers.IO){
                if (cachePolicy.readEnabled){
                    memoryCache[cacheKey]?.let {
                        return@withContext LinkPreviewResultSuccess(
                            preview = it,
                            fromMemoryCache = true
                        )
                    }
                }
                val doc = buildRequest(request).execute().parse()
                val preview = parseDocument(doc).also { preview ->
                    if (cachePolicy.writeEnabled) {
                        memoryCache[cacheKey] = preview
                    }
                }
                return@withContext LinkPreviewResultSuccess(
                    preview = preview,
                    fromMemoryCache = false
                )
            }
        } catch (t: Throwable){
            return LinkPreviewResultError(
                preview = null,
                throwable = t
            )
        }
    }

    private fun parseDocument(doc: Document): LinkPreview {
        val ogData = OpenGraphData.Builder()
        val metaTags = mutableListOf<MetaTag>()

        val pageTitle = doc.selectFirst("title")
            ?.text()
            ?.takeIf { it.isNotEmpty() }

        for (element in doc.select("meta")) {
            val name = element.attr("name")
            val property = element.attr("property")
            val content = element.attr("content")
            val key = name.ifEmpty { property }
            if(content.isEmpty()) continue
            if (key.isEmpty()) continue

            if (key.startsWith("og:")) {
                when (key) {
                    "og:title" -> ogData.title(content)
                    "og:type" -> ogData.type(OpenGraphType.fromString(content))
                    "og:image" -> ogData.image(content)
                    "og:url" -> ogData.url(content)
                    "og:audio" -> ogData.audio(content)
                    "og:description" -> ogData.description(content)
                    "og:determiner" -> ogData.determiner(content)
                    "og:locale" -> ogData.locale(content)
                    "og:site_name" -> ogData.siteName(content)
                    "og:video" -> ogData.video(content)
                    else -> ogData.appendTag(OpenGraphTag.Other(key.removePrefix("og:"), content))
                }
            } else {
                metaTags.add(MetaTag(key, content))
            }
        }

        return LinkPreview(
            pageTitle = pageTitle,
            openGraph = ogData.build(),
            metaTags = metaTags
        )
    }


    private fun buildRequest(r: LinkPreviewRequest) = Jsoup
        .connect(r.url)
        .method(r.method.method)
        .headers(r.headers)
        .cookies(r.cookies)
        .data(r.data)
        .proxy(r.proxy)
        .apply { r.userAgent?.let(::userAgent) }
        .apply { r.timeout?.let(::timeout) }
        .apply { r.referrer?.let(::referrer) }
        .apply { r.followRedirects?.let(::followRedirects) }
        .apply { r.ignoreHttpErrors?.let(::ignoreHttpErrors) }
        .apply { r.ignoreContentType?.let(::ignoreContentType) }
        .apply { r.requestBody?.let(::requestBody) }
        .apply { r.maxBodySize?.let(::maxBodySize) }
        .apply { r.postDataCharset?.let(::postDataCharset) }


    data class Options(
        val memoryCache: Lazy<LinkPreviewCache>,
        val memoryCachePolicy: CachePolicy
    )

}