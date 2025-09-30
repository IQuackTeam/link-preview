package ru.iquack.linkpreview.core

/**
 * Represents the OpenGraph type (og:type) for a web page.
 *
 * @property type The string value of the OpenGraph type (e.g., "website", "article").
 */
sealed class OpenGraphType(val type: String){
    /** Represents the "website" OpenGraph type. */
    object Website: OpenGraphType(WEBSITE)
    /** Represents the "article" OpenGraph type. */
    object Article: OpenGraphType(ARTICLE)
    /** Represents the "book" OpenGraph type. */
    object Book: OpenGraphType(BOOK)
    /** Represents the "profile" OpenGraph type. */
    object Profile: OpenGraphType(PROFILE)
    /** Represents the "music" OpenGraph type. */
    object Music: OpenGraphType(MUSIC)
    /** Represents the "video" OpenGraph type. */
    object Video: OpenGraphType(VIDEO)
    /** Represents any other custom or non-standard OpenGraph type. */
    class Other(type: String): OpenGraphType(type)

    companion object {
        /**
         * Parses a string value and returns the corresponding [OpenGraphType] instance.
         * If the value is not recognized, returns [Other].
         *
         * @param type The string value of the OpenGraph type.
         * @return The corresponding [OpenGraphType] instance.
         */
        internal fun fromString(type: String): OpenGraphType {
            return when (type.lowercase()) {
                WEBSITE -> Website
                ARTICLE -> Article
                BOOK -> Book
                PROFILE -> Profile
                MUSIC -> Music
                VIDEO -> Video
                else -> Other(type)
            }
        }

        private const val WEBSITE = "website"
        private const val ARTICLE = "article"
        private const val BOOK = "book"
        private const val PROFILE = "profile"
        private const val MUSIC = "music"
        private const val VIDEO = "video"
    }
}