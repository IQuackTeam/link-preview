package ru.iquack.linkpreview.core

import ru.iquack.linkpreview.core.memory.LinkPreviewCache
import ru.iquack.linkpreview.core.request.CachePolicy
import ru.iquack.linkpreview.core.request.LinkPreviewRequest
import ru.iquack.linkpreview.core.request.LinkPreviewResult


/**
 * Interface for loading link previews (web page metadata, OpenGraph, etc.).
 *
 * Provides methods for executing a link preview request and managing memory cache.
 */
interface LinkPreviewLoader {

    /**
     * The memory cache instance used for storing link previews, or null if disabled.
     */
    val memoryCache: LinkPreviewCache?

    /**
     * The cache policy for memory cache operations (read/write/disabled).
     */
    val memoryCachePolicy: CachePolicy

    /**
     * Executes a link preview request and returns the result.
     *
     * @param request The [LinkPreviewRequest] describing the target URL and options.
     * @return The [LinkPreviewResult] containing preview data or error.
     */
    suspend fun execute(request: LinkPreviewRequest): LinkPreviewResult

    /**
     * Builder for constructing [LinkPreviewLoader] instances with custom cache and policy.
     */
    class Builder {

        private var memoryCacheLazy: Lazy<LinkPreviewCache> = lazy { LinkPreviewCache.getDefault() }
        private var memoryCachePolicy: CachePolicy = CachePolicy.ENABLED

        /**
         * Sets the memory cache instance to use for this loader.
         * @param memoryCache The [LinkPreviewCache] instance or null to use default caching.
         * @return This builder instance.
         */
        fun memoryCache(memoryCache: LinkPreviewCache?) = apply {
            this.memoryCacheLazy = lazy { memoryCache?: LinkPreviewCache.getDefault() }
        }

        /**
         * Sets the memory cache policy (read/write/disabled).
         * @param memoryCachePolicy The [CachePolicy] to use.
         * @return This builder instance.
         */
        fun memoryCachePolicy(memoryCachePolicy: CachePolicy) = apply {
            this.memoryCachePolicy = memoryCachePolicy
        }

        /**
         * Builds a [LinkPreviewLoader] instance with the specified options.
         * @return The constructed [LinkPreviewLoader].
         */
        fun build(): LinkPreviewLoader{
            val options = RealLinkPreviewLoader.Options(
                memoryCache = memoryCacheLazy,
                memoryCachePolicy = memoryCachePolicy
            )
            return RealLinkPreviewLoader(options)
        }

    }

}