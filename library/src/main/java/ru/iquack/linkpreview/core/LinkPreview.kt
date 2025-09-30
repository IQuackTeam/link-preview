package ru.iquack.linkpreview.core

/**
 * Model of the result of Parsing Preverters for exile (Web page).
 *
 * @property pageTitle Page title (<title>), if found.
 * @property openGraph OpenGraph data extracted from META tags.
 * @property metaTags List of all META tugs found (name/property + content).
 */
data class LinkPreview(
    val pageTitle: String?,
    val openGraph: OpenGraphData,
    val metaTags: List<MetaTag>,
)

/**
 * It represents one META tag from an HTML document.
 *
 * @property name name or property Meta Tag (for example, "description", "og:title").
 * @property content Содержимое атрибута content.
 */
data class MetaTag(
    val name: String,
    val content: String
)