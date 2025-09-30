package ru.iquack.linkpreview.core

/**
 * Represents the OpenGraph metadata extracted from a web page.
 *
 * @property title The OpenGraph title (og:title) or null if not present.
 * @property type The OpenGraph type (og:type) or null if not present.
 * @property image The OpenGraph image URL (og:image) or null if not present.
 * @property url The OpenGraph canonical URL (og:url) or null if not present.
 * @property audio The OpenGraph audio URL (og:audio) or null if not present.
 * @property description The OpenGraph description (og:description) or null if not present.
 * @property determiner The OpenGraph determiner (og:determiner) or null if not present.
 * @property locale The OpenGraph locale (og:locale) or null if not present.
 * @property siteName The OpenGraph site name (og:site_name) or null if not present.
 * @property video The OpenGraph video URL (og:video) or null if not present.
 * @property tags All OpenGraph tags found in the document, including standard and custom tags.
 */
class OpenGraphData(
    val title: String?,
    val type: OpenGraphType?,
    val image: String?,
    val url: String?,
    val audio: String?,
    val description: String?,
    val determiner: String?,
    val locale: String?,
    val siteName: String?,
    val video: String?,
    val tags: List<OpenGraphTag>
){
    /**
     * Builder for constructing [OpenGraphData] instances in a flexible way.
     */
    class Builder{
        private var title: String? = null
        private var type: OpenGraphType? = null
        private var image: String? = null
        private var url: String? = null
        private var audio: String? = null
        private var description: String? = null
        private var determiner: String? = null
        private var locale: String? = null
        private var siteName: String? = null
        private var video: String? = null
        private val tags = mutableListOf<OpenGraphTag>()

        /** Sets the OpenGraph title (og:title). */
        fun title(title: String?) = apply {
            this.title = title
            if (title != null) tags.add(OpenGraphTag.Title(title))
        }
        /** Sets the OpenGraph type (og:type). */
        fun type(type: OpenGraphType?) = apply {
            this.type = type
            if (type != null) tags.add(OpenGraphTag.Type(type.type))
        }
        /** Sets the OpenGraph image URL (og:image). */
        fun image(image: String?) = apply {
            this.image = image
            if (image != null) tags.add(OpenGraphTag.Image(image))
        }
        /** Sets the OpenGraph canonical URL (og:url). */
        fun url(url: String?) = apply {
            this.url = url
            if (url != null) tags.add(OpenGraphTag.Url(url))
        }
        /** Sets the OpenGraph audio URL (og:audio). */
        fun audio(audio: String?) = apply {
            this.audio = audio
            if (audio != null) tags.add(OpenGraphTag.Audio(audio))
        }
        /** Sets the OpenGraph description (og:description). */
        fun description(description: String?) = apply {
            this.description = description
            if (description != null) tags.add(OpenGraphTag.Description(description))
        }
        /** Sets the OpenGraph determiner (og:determiner). */
        fun determiner(determiner: String?) = apply {
            this.determiner = determiner
            if (determiner != null) tags.add(OpenGraphTag.Determiner(determiner))
        }
        /** Sets the OpenGraph locale (og:locale). */
        fun locale(locale: String?) = apply {
            this.locale = locale
            if (locale != null) tags.add(OpenGraphTag.Locale(locale))
        }
        /** Sets the OpenGraph site name (og:site_name). */
        fun siteName(siteName: String?) = apply {
            this.siteName = siteName
            if (siteName != null) tags.add(OpenGraphTag.SiteName(siteName))
        }
        /** Sets the OpenGraph video URL (og:video). */
        fun video(video: String?) = apply {
            this.video = video
            if (video != null) tags.add(OpenGraphTag.Video(video))
        }
        /** Appends a custom or additional OpenGraph tag. */
        fun appendTag(tag: OpenGraphTag) = apply {
            this.tags.add(tag)
        }
        /** Appends a list of custom or additional OpenGraph tags. */
        fun appendTags(tags: List<OpenGraphTag>) = apply {
            this.tags.addAll(tags)
        }
        /** Appends multiple custom or additional OpenGraph tags. */
        fun appendTags(vararg tags: OpenGraphTag) = apply {
            this.tags.addAll(tags)
        }
        /** Clears all tags from the builder. */
        fun clearTags() = apply {
            this.tags.clear()
        }
        /**
         * Builds the [OpenGraphData] instance with the current builder state.
         * @return The constructed [OpenGraphData].
         */
        fun build(): OpenGraphData{
            return OpenGraphData(
                title = title,
                type = type,
                image = image,
                url = url,
                audio = audio,
                description = description,
                determiner = determiner,
                locale = locale,
                siteName = siteName,
                video = video,
                tags = tags.toList()
            )
        }
    }
}