package ru.iquack.linkpreview.compose

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import kotlinx.coroutines.CoroutineScope
import ru.iquack.linkpreview.core.LinkPreviewLoader
import ru.iquack.linkpreview.core.request.CachePolicy
import ru.iquack.linkpreview.core.request.LinkPreviewRequest

/**
 * Composable for displaying a link preview with subcomposition slots for each state.
 *
 * @param url The URL to load the preview for.
 * @param modifier Modifier for the root Box.
 * @param memoryCachePolicy The cache policy for memory cache.
 * @param requestBuilder Optional builder for customizing the LinkPreviewRequest.
 * @param loader The loader instance to use for fetching previews.
 * @param scope CoroutineScope for loading operations.
 * @param contentAlignment Alignment for the Box content.
 * @param propagateMinConstraints Whether to propagate minimum constraints to children.
 * @param success Slot for rendering when preview is successfully loaded.
 * @param failure Slot for rendering when an error occurs.
 * @param loading Slot for rendering while loading.
 * @param idle Slot for rendering when idle (no request).
 *
 * Example usage:
 * ```kotlin
 * SubcomposeLinkPreview(
 *     url = "https://example.com",
 *     loading = { CircularProgressIndicator() },
 *     failure = { Text("Error: ${'$'}{it.throwable.message}") },
 *     success = { LinkPreviewCard(it.preview) },
 *     idle = { /* Placeholder */ }
 * )
 * ```
 */
@Composable
fun SubcomposeLinkPreview(
    url: String,
    modifier: Modifier = Modifier,
    memoryCachePolicy: CachePolicy = CachePolicy.ENABLED,
    requestBuilder: (LinkPreviewRequest.Builder.() -> Unit)? = null,
    loader: LinkPreviewLoader = rememberLinkPreviewLoader(),
    scope: CoroutineScope = rememberCoroutineScope(),
    contentAlignment: Alignment = Alignment.TopStart,
    propagateMinConstraints: Boolean = false,
    success: @Composable (LinkPreviewState.Success) -> Unit,
    failure: @Composable (LinkPreviewState.Error) -> Unit,
    loading: @Composable (LinkPreviewState.Loading) -> Unit,
    idle: @Composable (LinkPreviewState.Idle) -> Unit,
) = SubcomposeLinkPreview(
    request = rememberLinkPreviewRequest(url, memoryCachePolicy, requestBuilder),
    modifier = modifier,
    loader = loader,
    scope = scope,
    contentAlignment = contentAlignment,
    propagateMinConstraints = propagateMinConstraints,
    success = success,
    failure = failure,
    loading = loading,
    idle = idle
)

/**
 * Composable for displaying a link preview with subcomposition slots for each state.
 *
 * @param request The [LinkPreviewRequest] to load the preview for.
 * @param modifier Modifier for the root Box.
 * @param loader The loader instance to use for fetching previews.
 * @param scope CoroutineScope for loading operations.
 * @param contentAlignment Alignment for the Box content.
 * @param propagateMinConstraints Whether to propagate minimum constraints to children.
 * @param success Slot for rendering when preview is successfully loaded.
 * @param failure Slot for rendering when an error occurs.
 * @param loading Slot for rendering while loading.
 * @param idle Slot for rendering when idle (no request).
 */
@Composable
fun SubcomposeLinkPreview(
    request: LinkPreviewRequest,
    modifier: Modifier = Modifier,
    loader: LinkPreviewLoader = rememberLinkPreviewLoader(),
    scope: CoroutineScope = rememberCoroutineScope(),
    contentAlignment: Alignment = Alignment.TopStart,
    propagateMinConstraints: Boolean = false,
    success: @Composable (LinkPreviewState.Success) -> Unit,
    failure: @Composable (LinkPreviewState.Error) -> Unit,
    loading: @Composable (LinkPreviewState.Loading) -> Unit,
    idle: @Composable (LinkPreviewState.Idle) -> Unit,
) = SubcomposeLinkPreview(
    request = request,
    modifier = modifier,
    loader = loader,
    scope = scope,
    contentAlignment = contentAlignment,
    propagateMinConstraints = propagateMinConstraints,
    content = {
        when (val s = state) {
            is LinkPreviewState.Success -> success(s)
            is LinkPreviewState.Error -> failure(s)
            is LinkPreviewState.Loading -> loading(s)
            is LinkPreviewState.Idle -> idle(s)
        }
    }
)

/**
 * Composable for displaying a link preview with a single content slot that receives the current state.
 *
 * @param url The URL to load the preview for.
 * @param modifier Modifier for the root Box.
 * @param memoryCachePolicy The cache policy for memory cache.
 * @param requestBuilder Optional builder for customizing the LinkPreviewRequest.
 * @param loader The loader instance to use for fetching previews.
 * @param scope CoroutineScope for loading operations.
 * @param contentAlignment Alignment for the Box content.
 * @param propagateMinConstraints Whether to propagate minimum constraints to children.
 * @param content Slot that receives the current [SubcomposeLinkPreviewScope] with [LinkPreviewState].
 */
@Composable
fun SubcomposeLinkPreview(
    url: String,
    modifier: Modifier = Modifier,
    memoryCachePolicy: CachePolicy = CachePolicy.ENABLED,
    requestBuilder: (LinkPreviewRequest.Builder.() -> Unit)? = null,
    loader: LinkPreviewLoader = rememberLinkPreviewLoader(),
    scope: CoroutineScope = rememberCoroutineScope(),
    contentAlignment: Alignment = Alignment.TopStart,
    propagateMinConstraints: Boolean = false,
    content: @Composable SubcomposeLinkPreviewScope.() -> Unit,
) = SubcomposeLinkPreview(
    request = rememberLinkPreviewRequest(url, memoryCachePolicy, requestBuilder),
    modifier = modifier,
    loader = loader,
    scope = scope,
    contentAlignment = contentAlignment,
    propagateMinConstraints = propagateMinConstraints,
    content = content
)

/**
 * Composable for displaying a link preview with a single content slot that receives the current state.
 *
 * @param request The [LinkPreviewRequest] to load the preview for.
 * @param modifier Modifier for the root Box.
 * @param loader The loader instance to use for fetching previews.
 * @param scope CoroutineScope for loading operations.
 * @param contentAlignment Alignment for the Box content.
 * @param propagateMinConstraints Whether to propagate minimum constraints to children.
 * @param content Slot that receives the current [SubcomposeLinkPreviewScope] with [LinkPreviewState].
 */
@Composable
fun SubcomposeLinkPreview(
    request: LinkPreviewRequest,
    modifier: Modifier = Modifier,
    loader: LinkPreviewLoader = rememberLinkPreviewLoader(),
    scope: CoroutineScope = rememberCoroutineScope(),
    contentAlignment: Alignment = Alignment.TopStart,
    propagateMinConstraints: Boolean = false,
    content: @Composable SubcomposeLinkPreviewScope.() -> Unit,
) = SubcomposeLinkPreview(
    state = rememberLinkPreviewState(
        request = request,
        loader = loader,
        scope = scope
    ),
    modifier = modifier,
    loader = loader,
    scope = scope,
    contentAlignment = contentAlignment,
    propagateMinConstraints = propagateMinConstraints,
    content = content
)

@Composable
fun SubcomposeLinkPreview(
    state: LinkPreviewState,
    modifier: Modifier = Modifier,
    loader: LinkPreviewLoader = rememberLinkPreviewLoader(),
    scope: CoroutineScope = rememberCoroutineScope(),
    contentAlignment: Alignment = Alignment.TopStart,
    propagateMinConstraints: Boolean = false,
    success: @Composable (LinkPreviewState.Success) -> Unit,
    failure: @Composable (LinkPreviewState.Error) -> Unit,
    loading: @Composable (LinkPreviewState.Loading) -> Unit,
    idle: @Composable (LinkPreviewState.Idle) -> Unit,
) = SubcomposeLinkPreview(
    state = state,
    modifier = modifier,
    loader = loader,
    scope = scope,
    contentAlignment = contentAlignment,
    propagateMinConstraints = propagateMinConstraints,
    content = {
        when (val s = state) {
            is LinkPreviewState.Success -> success(s)
            is LinkPreviewState.Error -> failure(s)
            is LinkPreviewState.Loading -> loading(s)
            is LinkPreviewState.Idle -> idle(s)
        }
    }
)

/**
 * Displays a link preview using a provided [LinkPreviewState], suitable for use with [LinkPreviewStateHolder].
 *
 * This overload allows you to manage the preview state externally (e.g., in lists with [LinkPreviewStateHolder])
 * and pass it directly to the composable for rendering. The [content] lambda receives a [SubcomposeLinkPreviewScope]
 * with the current [LinkPreviewState].
 *
 * @param state The externally managed [LinkPreviewState] to display.
 * @param modifier Modifier for the root Box.
 * @param loader The loader instance (not used in this overload, but kept for API consistency).
 * @param scope CoroutineScope for loading operations (not used in this overload, but kept for API consistency).
 * @param contentAlignment Alignment for the Box content.
 * @param propagateMinConstraints Whether to propagate minimum constraints to children.
 * @param content Slot that receives the current [SubcomposeLinkPreviewScope] with [LinkPreviewState].
 *
 * Example usage with [LinkPreviewStateHolder]:
 * ```kotlin
 * val stateHolder = rememberLinkPreviewStateHolder()
 * val state = stateHolder.getState(url)
 * SubcomposeLinkPreview(state = state) { ... }
 * ```
 */
@Composable
fun SubcomposeLinkPreview(
    state: LinkPreviewState,
    modifier: Modifier = Modifier,
    loader: LinkPreviewLoader = rememberLinkPreviewLoader(),
    scope: CoroutineScope = rememberCoroutineScope(),
    contentAlignment: Alignment = Alignment.TopStart,
    propagateMinConstraints: Boolean = false,
    content: @Composable SubcomposeLinkPreviewScope.() -> Unit,
) = Box(
    modifier = modifier,
    contentAlignment = contentAlignment,
    propagateMinConstraints = propagateMinConstraints,
    content = { RealSubcomposeLinkPreviewScope(
        parentScope = this,
        state = state,
        loader = loader,
        scope = scope
    ).content() }
)

/**
 * Scope for subcomposition slots in [SubcomposeLinkPreview].
 * Provides access to the current [LinkPreviewState] and delegates to [BoxScope].
 */
@Stable
interface SubcomposeLinkPreviewScope{
    /** The current state of the link preview. */
    val state: LinkPreviewState
    /** The [LinkPreviewLoader] instance used for loading previews. */
    val loader: LinkPreviewLoader
    /** The [CoroutineScope] used for loading operations. */
    val scope: CoroutineScope
}

internal class RealSubcomposeLinkPreviewScope(
    private val parentScope: BoxScope,
    override val state: LinkPreviewState,
    override val loader: LinkPreviewLoader,
    override val scope: CoroutineScope
): SubcomposeLinkPreviewScope, BoxScope by parentScope