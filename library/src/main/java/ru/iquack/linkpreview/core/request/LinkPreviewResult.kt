package ru.iquack.linkpreview.core.request

import ru.iquack.linkpreview.core.LinkPreview

/**
 * Represents the result of a link preview request.
 * Can be either [LinkPreviewResultSuccess] or [LinkPreviewResultError].
 * @property preview The parsed [LinkPreview] if available, or null otherwise.
 */
sealed interface LinkPreviewResult {
    /**
     * The parsed [LinkPreview] if available, or null otherwise.
     */
    val preview: LinkPreview?
}

/**
 * Represents a successful link preview result.
 * @property preview The parsed [LinkPreview].
 * @property fromMemoryCache True if the result was loaded from memory cache, false if loaded from network.
 */
data class LinkPreviewResultSuccess(
    override val preview: LinkPreview,
    val fromMemoryCache: Boolean
): LinkPreviewResult

/**
 * Represents an error that occurred during link preview loading or parsing.
 * @property preview The parsed [LinkPreview], if any, or null.
 * @property throwable The exception that was thrown.
 */
data class LinkPreviewResultError(
    override val preview: LinkPreview? = null,
    val throwable: Throwable
): LinkPreviewResult

/**
 * Returns true if this result is [LinkPreviewResultSuccess].
 */
val LinkPreviewResult.isSuccess: Boolean
    get() = this is LinkPreviewResultSuccess

/**
 * Returns true if this result is [LinkPreviewResultError].
 */
val LinkPreviewResult.isError: Boolean
    get() = this is LinkPreviewResultError
