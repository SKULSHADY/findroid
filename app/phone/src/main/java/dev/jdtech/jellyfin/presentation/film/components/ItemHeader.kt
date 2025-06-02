package dev.jdtech.jellyfin.presentation.film.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import dev.jdtech.jellyfin.models.FindroidEpisode
import dev.jdtech.jellyfin.models.FindroidItem
import dev.jdtech.jellyfin.presentation.theme.spacings
import dev.jdtech.jellyfin.presentation.utils.parallaxLayoutModifier

@Composable
fun ItemHeader(
    item: FindroidItem,
    scrollState: ScrollState,
    showLogo: Boolean = true,
    navigateToItem: (item: FindroidItem) -> Unit = {},
) {
    val backgroundColor = MaterialTheme.colorScheme.background

    val backdropUri = when (item) {
        is FindroidEpisode -> item.images.primary
        else -> item.images.backdrop
    }

    val logoUri = when (item) {
        is FindroidEpisode -> item.images.showLogo
        else -> item.images.logo
    }

    Box(
        modifier = Modifier
            .height(360.dp)
            .clipToBounds()
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ) {
                navigateToItem(item)
            },
    ) {
        AsyncImage(
            model = backdropUri,
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .height(320.dp)
                .parallaxLayoutModifier(
                    scrollState = scrollState,
                    2,
                ),
            placeholder = ColorPainter(MaterialTheme.colorScheme.surfaceContainer),
            contentScale = ContentScale.Crop,
        )
        Canvas(
            modifier = Modifier
                .fillMaxSize(),
        ) {
            drawRect(
                brush = Brush.verticalGradient(
                    colors = listOf(Color.Transparent, backgroundColor),
                    startY = size.height / 2,
                    endY = size.height * 0.85f,
                ),
            )
        }
        if (showLogo) {
            AsyncImage(
                model = logoUri,
                contentDescription = null,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(
                        start = MaterialTheme.spacings.large,
                        end = MaterialTheme.spacings.large,
                        top = MaterialTheme.spacings.default,
                        bottom = MaterialTheme.spacings.default,
                    )
                    .height(100.dp)
                    .fillMaxWidth(),
                contentScale = ContentScale.Fit,
            )
            Spacer(Modifier.height(MaterialTheme.spacings.large))
        }
    }
}
