package dev.jdtech.jellyfin.presentation.film.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import coil3.compose.AsyncImage
import dev.jdtech.jellyfin.models.FindroidEpisode
import dev.jdtech.jellyfin.models.FindroidItem
import dev.jdtech.jellyfin.models.FindroidMovie
import dev.jdtech.jellyfin.models.FindroidSeason
import dev.jdtech.jellyfin.models.FindroidShow

enum class Direction {
    HORIZONTAL, VERTICAL
}

@Composable
fun ItemPoster(
    item: FindroidItem,
    direction: Direction,
    modifier: Modifier = Modifier,
    forceThumbnail: Boolean = false,
) {
    var imageUri = item.images.primary

    when (direction) {
        Direction.HORIZONTAL -> {
            when (item) {
                is FindroidMovie -> imageUri = item.images.thumb
                is FindroidEpisode -> if (imageUri == null || forceThumbnail) imageUri = item.images.showThumb
            }
        }

        Direction.VERTICAL -> {
            when (item) {
                is FindroidEpisode -> imageUri = item.images.showPrimary
                is FindroidSeason -> if (imageUri == null) imageUri = item.images.showPrimary
            }
        }
    }

    AsyncImage(
        model = imageUri,
        contentDescription = null,
        contentScale = ContentScale.Crop,
        modifier = modifier
            .fillMaxWidth()
            .aspectRatio(if (direction == Direction.HORIZONTAL) 1.77f else 0.66f)
            .background(
                MaterialTheme.colorScheme.surfaceContainer,
            ),
    )
}
