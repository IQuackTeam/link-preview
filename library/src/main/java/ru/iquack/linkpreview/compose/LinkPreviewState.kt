package ru.iquack.linkpreview.compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import ru.iquack.linkpreview.core.LinkPreview
import ru.iquack.linkpreview.core.LinkPreviewLoader
import ru.iquack.linkpreview.core.memory.LinkPreviewCache
import ru.iquack.linkpreview.core.request.CachePolicy
import ru.iquack.linkpreview.core.request.LinkPreviewRequest
import ru.iquack.linkpreview.core.request.LinkPreviewResultError
import ru.iquack.linkpreview.core.request.LinkPreviewResultSuccess


/**
 * Represents the state of a link preview request in Compose.
 */
@Stable
sealed interface LinkPreviewState{
    /** Idle state: no request is being made. */
    object Idle : LinkPreviewState
    /** Loading state: the preview is being loaded. */
    object Loading : LinkPreviewState
    /** Success state: the preview was loaded successfully. */
    data class Success(val preview: LinkPreview) : LinkPreviewState
    /** Error state: an error occurred while loading the preview. */
    data class Error(val throwable: Throwable?) : LinkPreviewState
}


/**
 * Remembers and manages the state of a link preview request for the given URL.
 *
 * @param url The URL to load the preview for.
 * @param memoryCachePolicy The cache policy for memory cache.
 * @param loader The loader instance to use for fetching previews.
 * @param scope CoroutineScope for loading operations.
 * @param transform Optional transformation function to modify the state.
 * @param requestBuilder Optional builder for customizing the LinkPreviewRequest.
 * @return The current [LinkPreviewState].
 */
@Composable
fun rememberLinkPreviewState(
    url: String,
    memoryCachePolicy: CachePolicy = CachePolicy.ENABLED,
    loader: LinkPreviewLoader = rememberLinkPreviewLoader(),
    scope: CoroutineScope = rememberCoroutineScope(),
    transform: (LinkPreviewState) -> LinkPreviewState = { it },
    requestBuilder: (LinkPreviewRequest.Builder.() -> Unit)? = null,
): LinkPreviewState = rememberLinkPreviewState(
    request = rememberLinkPreviewRequest(
        url = url,
        memoryCachePolicy = memoryCachePolicy,
        requestBuilder = requestBuilder
    ),
    loader = loader,
    scope = scope,
    transform = transform
)

/**
 * Remembers and manages the state of a link preview request for the given [LinkPreviewRequest].
 *
 * @param request The [LinkPreviewRequest] to load the preview for.
 * @param loader The loader instance to use for fetching previews.
 * @param scope CoroutineScope for loading operations.
 * @param transform Optional transformation function to modify the state.
 * @return The current [LinkPreviewState].
 */
@Composable
fun rememberLinkPreviewState(
    request: LinkPreviewRequest,
    loader: LinkPreviewLoader = rememberLinkPreviewLoader(),
    scope: CoroutineScope = rememberCoroutineScope(),
    transform: (LinkPreviewState) -> LinkPreviewState = { it }
): LinkPreviewState {
    var state by remember(request) { mutableStateOf(transform(LinkPreviewState.Idle)) }

    LaunchedEffect(request) {
        if (request.url.isEmpty()) {
            state = transform(LinkPreviewState.Idle)
            return@LaunchedEffect
        }
        state = transform(LinkPreviewState.Loading)
        scope.launch {
            val result = loader.execute(request)
            state = when (result) {
                is LinkPreviewResultSuccess -> LinkPreviewState.Success(result.preview)
                is LinkPreviewResultError -> LinkPreviewState.Error(result.throwable)
            }.let(transform)
        }
    }
    return state
}

/**
 * Remembers and provides a [LinkPreviewLoader] instance with the given cache and policy.
 *
 * @param memoryCache The memory cache instance to use, or null to disable caching.
 * @param memoryCachePolicy The cache policy for memory cache.
 * @return The [LinkPreviewLoader] instance.
 */
@Composable
fun rememberLinkPreviewLoader(
    memoryCache: LinkPreviewCache? = null,
    memoryCachePolicy: CachePolicy = CachePolicy.ENABLED
): LinkPreviewLoader = rememberLinkPreviewLoader{
    memoryCache(memoryCache)
    memoryCachePolicy(memoryCachePolicy)
}

/**
 * Remembers and provides a [LinkPreviewLoader] instance using a custom builder.
 *
 * @param requestBuilder Builder for configuring the [LinkPreviewLoader].
 * @return The [LinkPreviewLoader] instance.
 */
@Composable
fun rememberLinkPreviewLoader(
    requestBuilder: LinkPreviewLoader.Builder.() -> Unit,
): LinkPreviewLoader = remember(requestBuilder){
    LinkPreviewLoader.Builder()
        .apply(requestBuilder)
        .build()
}

/**
 * Remembers and builds a [LinkPreviewRequest] for the given URL and options.
 *
 * @param url The URL to load the preview for.
 * @param memoryCachePolicy The cache policy for memory cache.
 * @param requestBuilder Optional builder for customizing the [LinkPreviewRequest].
 * @return The [LinkPreviewRequest] instance.
 */
@Composable
fun rememberLinkPreviewRequest(
    url: String,
    memoryCachePolicy: CachePolicy = CachePolicy.ENABLED,
    requestBuilder: (LinkPreviewRequest.Builder.() -> Unit)? = null
): LinkPreviewRequest = remember(url, memoryCachePolicy, requestBuilder) {
    val builder = LinkPreviewRequest.Builder()
        .url(url)
        .memoryCachePolicy(memoryCachePolicy)
    if (requestBuilder != null) builder.apply(requestBuilder)
    builder.build()
}