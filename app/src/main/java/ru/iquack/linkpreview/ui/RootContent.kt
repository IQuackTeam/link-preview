package ru.iquack.linkpreview.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.unit.dp
import ru.iquack.linkpreview.R
import ru.iquack.linkpreview.compose.rememberLinkPreviewStateHolder
import ru.iquack.linkpreview.ui.theme.LinkPreviewTheme

@Composable
internal fun RootContent() = LinkPreviewTheme {
    val urls = stringArrayResource(R.array.sample_urls)
    val stateHolder = rememberLinkPreviewStateHolder()
    LazyColumn(
        modifier = Modifier.systemBarsPadding(),
        contentPadding = PaddingValues(
            horizontal = 16.dp,
            vertical = 8.dp
        ),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ){
        items(urls){ url ->
            val state = stateHolder.getState(url){
                followRedirects(true)
            }
            LinkPreviewCardExample(state)
        }
    }
}