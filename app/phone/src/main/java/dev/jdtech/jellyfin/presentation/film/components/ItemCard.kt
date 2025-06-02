package dev.jdtech.jellyfin.presentation.film.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.jdtech.jellyfin.core.R
import dev.jdtech.jellyfin.core.presentation.dummy.dummyEpisode
import dev.jdtech.jellyfin.core.presentation.dummy.dummyMovie
import dev.jdtech.jellyfin.models.FindroidEpisode
import dev.jdtech.jellyfin.models.FindroidItem
import dev.jdtech.jellyfin.models.FindroidSeason
import dev.jdtech.jellyfin.models.FindroidShow
import dev.jdtech.jellyfin.presentation.theme.FindroidTheme
import dev.jdtech.jellyfin.presentation.theme.spacings

@Composable
fun ItemCard(
    item: FindroidItem,
    direction: Direction,
    onClick: (FindroidItem) -> Unit,
    modifier: Modifier = Modifier,
    forceThumbnail: Boolean = false,
) {
    val width = when (direction) {
        Direction.HORIZONTAL -> 164
        Direction.VERTICAL -> 104
    }
    Column(
        modifier = modifier
            .width(width.dp)
            .clip(MaterialTheme.shapes.small)
            .clickable(
                onClick = {
                    onClick(item)
                },
            ),
    ) {
        Surface(
            shape = MaterialTheme.shapes.large,
        ) {
            Box {
                ItemPoster(
                    item = item,
                    direction = direction,
                    forceThumbnail = forceThumbnail,
                )
                ProgressBadge(
                    item = item,
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(MaterialTheme.spacings.small)
                        .shadow(2.dp, MaterialTheme.shapes.extraLarge),
                )
                if (direction == Direction.HORIZONTAL) {
                    ProgressBar(
                        item = item,
                        modifier = Modifier
                            .align(Alignment.BottomStart)
                            .padding(
                                horizontal = 12.dp,
                                vertical = MaterialTheme.spacings.small
                            ),
                    )
                    ProgressBar(
                        item = item,
                        width = width,
                        modifier = Modifier
                            .align(Alignment.BottomStart)
                            .padding(
                                horizontal = 12.dp,
                                vertical = MaterialTheme.spacings.small
                            ),
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(MaterialTheme.spacings.small))
        Text(
            text = if (item is FindroidEpisode) item.seriesName else item.name,
            style = MaterialTheme.typography.bodyMedium,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.padding(horizontal = 2.dp),
        )
        val text =
            when (item) {
                is FindroidEpisode -> {
                    stringResource(
                        id = R.string.episode_name_extended,
                        item.parentIndexNumber,
                        item.indexNumber,
                        item.name,
                    )
                }
                else -> {
                    item.productionYear.toString()
                }
            }
        if (text != "null") {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(horizontal = 2.dp),
            ) {
                Text(
                    text = text,
                    style = MaterialTheme.typography.bodySmall,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.alpha(0.8f),
                )
                if (item !is FindroidEpisode && item.communityRating != null) {
                    Spacer(modifier = Modifier.width(MaterialTheme.spacings.small))
                    Icon(
                        painter = painterResource(R.drawable.ic_star),
                        contentDescription = null,
                        modifier = Modifier
                            .height(16.dp)
                            .padding(bottom = 1.dp)
                    )
                    Text(
                        text = "%.1f".format(item.communityRating),
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier
                            .padding(
                                start = 2.dp
                            )
                            .alpha(0.8f),
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ItemCardPreviewMovie() {
    FindroidTheme {
        ItemCard(
            item = dummyMovie,
            direction = Direction.HORIZONTAL,
            onClick = {},
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun ItemCardPreviewMovieVertical() {
    FindroidTheme {
        ItemCard(
            item = dummyMovie,
            direction = Direction.VERTICAL,
            onClick = {},
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun ItemCardPreviewEpisode() {
    FindroidTheme {
        ItemCard(
            item = dummyEpisode,
            direction = Direction.HORIZONTAL,
            onClick = {},
        )
    }
}
