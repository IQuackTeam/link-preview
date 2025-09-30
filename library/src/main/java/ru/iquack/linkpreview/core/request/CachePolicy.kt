package ru.iquack.linkpreview.core.request

/***
 * Policy for reading from and writing to the cache.
 * Note that even if reading is enabled, there is no guarantee that the preview will be in the cache.
 * Similarly, even if writing is enabled, there is no guarantee that the preview will be written to the cache
 * (for example, if the preview is not suitable for caching).
 */
enum class CachePolicy(
    val readEnabled: Boolean,
    val writeEnabled: Boolean,
) {
    /** Read from and write to the cache */
    ENABLED(true, true),
    /** Only read from the cache */
    READ_ONLY(true, false),
    /** Only write to the cache */
    WRITE_ONLY(false, true),
    /** Do not read from or write to the cache */
    DISABLED(false, false),
}