package ru.iquack.linkpreview.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import ru.iquack.linkpreview.compose.LinkPreviewState
import ru.iquack.linkpreview.compose.SubcomposeLinkPreview
import ru.iquack.linkpreview.core.OpenGraphTag

/**
 * Example of a beautiful and reusable LinkPreview card for Compose UI.
 *
 * @param state The [LinkPreviewState] to display.
 * @param modifier Modifier for the card.
 */
@Composable
internal fun LinkPreviewCardExample(
    state: LinkPreviewState,
    modifier: Modifier = Modifier
) = SubcomposeLinkPreview(
    modifier = modifier,
    state = state,
    loading = { LinkPreviewCardLoading(Modifier.fillMaxWidth()) },
    failure = { LinkPreviewCardError(it) },
    idle = { Spacer(Modifier.height(8.dp)) },
    success = { LinkPreviewCardSuccess(it) }
)

@Composable
private fun LinkPreviewCardLoading(
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        tonalElevation = 2.dp,
        shadowElevation = 4.dp,
        color = MaterialTheme.colorScheme.surface
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 120.dp)
                .padding(12.dp),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    }
}

@Composable
private fun LinkPreviewCardError(
    state: LinkPreviewState.Error,
    modifier: Modifier = Modifier
) {
    val message = state.throwable?.localizedMessage ?: state.throwable?.message ?: "Unknown error"
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        tonalElevation = 2.dp,
        shadowElevation = 4.dp,
        color = MaterialTheme.colorScheme.surface
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 120.dp)
                .padding(12.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = message,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.error
            )
        }
    }
}

@Composable
private fun LinkPreviewCardSuccess(
    state: LinkPreviewState.Success,
    modifier: Modifier = Modifier
) {
    val preview = state.preview
    val imageUrl = preview.openGraph.image
    val imageWidth = preview.openGraph.tags
        .find { (it as? OpenGraphTag.Other)?.tagName == "image:width" }
        ?.content?.toIntOrNull() ?: 0
    val imageHeight = preview.openGraph.tags
        .find { (it as? OpenGraphTag.Other)?.tagName == "image:height" }
        ?.content?.toIntOrNull() ?: 0
    val title = preview.openGraph.title ?: preview.pageTitle ?: "No title"
    val description = preview.openGraph.description
    val siteName = preview.openGraph.siteName ?: preview.openGraph.url?.let { url ->
        runCatching { java.net.URI(url).host?.removePrefix("www.") }.getOrNull() ?: url
    }
    val isLargeImage = imageWidth > 0 && imageHeight > 0 && imageWidth > imageHeight

    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        tonalElevation = 2.dp,
        shadowElevation = 4.dp,
        color = MaterialTheme.colorScheme.surface
    ) {
        Column {
            if (isLargeImage && !imageUrl.isNullOrBlank()) {
                AsyncImage(
                    model = imageUrl,
                    contentDescription = preview.openGraph.title,
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.LightGray),
                    contentScale = ContentScale.Crop
                )
                Spacer(Modifier.width(16.dp))
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 120.dp)
                    .padding(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (!imageUrl.isNullOrEmpty() && !isLargeImage) {
                    AsyncImage(
                        model = imageUrl,
                        contentDescription = preview.openGraph.title,
                        modifier = Modifier
                            .size(72.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color.LightGray),
                        contentScale = ContentScale.Crop
                    )
                    Spacer(Modifier.width(16.dp))
                }
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleMedium,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                    if (!description.isNullOrBlank()) {
                        Spacer(Modifier.height(4.dp))
                        Text(
                            text = description,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            maxLines = 3,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                    if (!siteName.isNullOrBlank()) {
                        Spacer(Modifier.height(8.dp))
                        Text(
                            text = siteName,
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.primary,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
            }
        }
    }
}
