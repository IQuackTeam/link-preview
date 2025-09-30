package ru.iquack.linkpreview.core

/**
 * Represents a single OpenGraph tag extracted from a web page.
 *
 * @property content The value/content of the OpenGraph tag.
 */
sealed class OpenGraphTag(
    val content: String
){
    /** Represents the og:title tag. */
    class Title(content: String): OpenGraphTag(content)
    /** Represents the og:type tag. */
    class Type(content: String): OpenGraphTag(content)
    /** Represents the og:image tag. */
    class Image(content: String): OpenGraphTag(content)
    /** Represents the og:url tag. */
    class Url(content: String): OpenGraphTag(content)
    /** Represents the og:description tag. */
    class Description(content: String): OpenGraphTag(content)
    /** Represents the og:determiner tag. */
    class Determiner(content: String): OpenGraphTag(content)
    /** Represents the og:locale tag. */
    class Locale(content: String): OpenGraphTag(content)
    /** Represents the og:site_name tag. */
    class SiteName(content: String): OpenGraphTag(content)
    /** Represents the og:video tag. */
    class Video(content: String): OpenGraphTag(content)
    /** Represents the og:audio tag. */
    class Audio(content: String): OpenGraphTag(content)
    /**
     * Represents any other custom or non-standard OpenGraph tag.
     *
     * @property tagName The name of the tag (without the "og:" prefix).
     */
    class Other(name: String, content: String): OpenGraphTag(content){
        val tagName = name.removePrefix("og:")
    }
}