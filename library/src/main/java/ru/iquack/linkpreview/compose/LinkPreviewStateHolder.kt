package ru.iquack.linkpreview.compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import ru.iquack.linkpreview.core.LinkPreviewLoader
import ru.iquack.linkpreview.core.request.CachePolicy
import ru.iquack.linkpreview.core.request.LinkPreviewRequest
import ru.iquack.linkpreview.core.request.LinkPreviewResultError
import ru.iquack.linkpreview.core.request.LinkPreviewResultSuccess

/**
 * Holds and manages LinkPreviewState for multiple URLs, suitable for use in lists (e.g., LazyColumn).
 *
 * @param loader The loader instance to use for fetching previews.
 * @param scope CoroutineScope for loading operations.
 * @param transform Optional transformation function to modify the state.
 */
class LinkPreviewStateHolder(
    private val loader: LinkPreviewLoader,
    private val scope: CoroutineScope,
    private val transform: (LinkPreviewState) -> LinkPreviewState = { it }
) {
    private val stateMap = mutableMapOf<String, MutableState<LinkPreviewState>>()

    /**
     * Returns the current state for the given URL, and triggers loading if needed.
     *
     * @param url The URL to load the preview for.
     * @param memoryCachePolicy The cache policy for memory cache.
     * @param requestBuilder Optional builder for customizing the LinkPreviewRequest.
     * @return The current [LinkPreviewState] for the URL.
     */
    @Composable
    fun getState(
        url: String,
        memoryCachePolicy: CachePolicy = CachePolicy.ENABLED,
        requestBuilder: (LinkPreviewRequest.Builder.() -> Unit)? = null
    ): LinkPreviewState {
        val state = stateMap.getOrPut(url) { mutableStateOf(transform(LinkPreviewState.Idle)) }

        if (state.value is LinkPreviewState.Idle && url.isNotEmpty()) {
            LaunchedEffect(url) {
                state.value = transform(LinkPreviewState.Loading)
                scope.launch {
                    val request = LinkPreviewRequest.Builder()
                        .url(url)
                        .memoryCachePolicy(memoryCachePolicy)
                        .apply { requestBuilder?.invoke(this) }
                        .build()
                    val result = loader.execute(request)
                    state.value = when (result) {
                        is LinkPreviewResultSuccess -> LinkPreviewState.Success(result.preview)
                        is LinkPreviewResultError -> LinkPreviewState.Error(result.throwable)
                    }.let(transform)
                }
            }
        }
        return state.value
    }
}

/**
 * Remembers a [LinkPreviewStateHolder] for managing link preview states in lists.
 *
 * @param loader The loader instance to use for fetching previews.
 * @param scope CoroutineScope for loading operations.
 * @param transform Optional transformation function to modify the state.
 * @return The [LinkPreviewStateHolder] instance.
 */
@Composable
fun rememberLinkPreviewStateHolder(
    loader: LinkPreviewLoader = rememberLinkPreviewLoader(),
    scope: CoroutineScope = rememberCoroutineScope(),
    transform: (LinkPreviewState) -> LinkPreviewState = { it }
): LinkPreviewStateHolder = remember(loader, scope, transform) {
    LinkPreviewStateHolder(loader, scope, transform)
}
