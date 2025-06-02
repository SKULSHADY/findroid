package dev.jdtech.jellyfin.presentation.film.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import dev.jdtech.jellyfin.core.R
import dev.jdtech.jellyfin.core.presentation.dummy.dummyMovie
import dev.jdtech.jellyfin.film.presentation.home.HomeAction
import dev.jdtech.jellyfin.models.FindroidItem
import dev.jdtech.jellyfin.models.FindroidMovie
import dev.jdtech.jellyfin.models.FindroidShow
import dev.jdtech.jellyfin.presentation.theme.FindroidTheme
import dev.jdtech.jellyfin.presentation.theme.spacings

@Composable
fun HomeCarouselItem(
    item: FindroidItem,
    onAction: (HomeAction) -> Unit,
    isLoadingPlayer: Boolean = false,
    isLoadingRestartPlayer: Boolean = false,
) {
    val backgroundColor = MaterialTheme.colorScheme.background
    val colorStops = arrayOf(
        0f to backgroundColor.copy(alpha = 0.1f),
        0.55f to backgroundColor.copy(alpha = 0.65f),
        1f to backgroundColor.copy(alpha = 0.8f),
    )

    Box(
        modifier = Modifier
            .clip(MaterialTheme.shapes.extraLarge)
            .aspectRatio(0.9f)
            .clickable {
                onAction(HomeAction.OnItemClick(item))
            }
            .fillMaxWidth(),
    ) {
        AsyncImage(
            model = item.images.backdrop,
            placeholder = ColorPainter(MaterialTheme.colorScheme.surfaceContainer),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth(),
        )
        Canvas(
            modifier = Modifier
                .fillMaxSize(),
        ) {
            drawRect(
                brush = Brush.verticalGradient(
                    colorStops = colorStops,
                ),
            )
        }
        Box(
            modifier = Modifier
                .padding(MaterialTheme.spacings.default)
                .align(Alignment.TopEnd)
                .shadow(
                    elevation = 2.dp,
                    shape = RoundedCornerShape(MaterialTheme.spacings.extraLarge)
                ),
        ) {
            Box(
                modifier = Modifier
                    .height(32.dp)
                    .defaultMinSize(32.dp)
                    .clip(MaterialTheme.shapes.extraLarge)
                    .background(MaterialTheme.colorScheme.primary)
                    .padding(horizontal = MaterialTheme.spacings.extraSmall)
                    .align(Alignment.TopEnd),
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(start = MaterialTheme.spacings.extraSmall)
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_star),
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onPrimary,
                        modifier = Modifier.height(20.dp)
                    )
                    Text(
                        text = "%.1f".format(item.communityRating),
                        color = MaterialTheme.colorScheme.onPrimary,
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier
                            .align(Alignment.CenterVertically)
                            .padding(
                                start = MaterialTheme.spacings.extraSmall,
                                end = MaterialTheme.spacings.small
                            ),
                        fontWeight = FontWeight.Bold,
                    )
                }
            }
        }
        Column(
            verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacings.small),
            modifier = Modifier
                .padding(
                    horizontal = MaterialTheme.spacings.default,
                    vertical = MaterialTheme.spacings.default,
                )
                .align(Alignment.BottomStart)
                .onGloballyPositioned { coordinates ->
                    coordinates.size
                },
        ) {
            val genres = when (item) {
                is FindroidMovie -> item.genres
                is FindroidShow -> item.genres
                else -> emptyList()
            }
            Text(
                text = genres.joinToString(),
                overflow = TextOverflow.Ellipsis,
                maxLines = 1,
                style = MaterialTheme.typography.labelMedium,
                modifier = Modifier.alpha(0.8f),
            )
            Text(
                text = item.name,
                modifier = Modifier,
                overflow = TextOverflow.Ellipsis,
                maxLines = 2,
                style = MaterialTheme.typography.headlineMedium,
            )
            Text(
                text = item.overview,
                modifier = Modifier.alpha(0.8f),
                overflow = TextOverflow.Ellipsis,
                maxLines = 2,
                style = MaterialTheme.typography.bodySmall,
            )
            Spacer(modifier = Modifier.height(MaterialTheme.spacings.small))
            PlayButton(
                item = item,
                onClick = {
                    onAction(HomeAction.OnPlayClick(item))
                },
                enabled = !isLoadingPlayer && !isLoadingRestartPlayer,
                isLoading = isLoadingPlayer
            )
        }
    }
}

@Composable
@Preview(showBackground = true)
private fun HomeCarouselItemPreview() {
    FindroidTheme {
        HomeCarouselItem(
            item = dummyMovie,
            onAction = {},
            isLoadingPlayer = false,
            isLoadingRestartPlayer = false,
        )
    }
}
