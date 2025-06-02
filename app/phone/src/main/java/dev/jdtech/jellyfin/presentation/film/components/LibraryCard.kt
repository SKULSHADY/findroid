package dev.jdtech.jellyfin.presentation.film.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.jdtech.jellyfin.core.R
import dev.jdtech.jellyfin.core.presentation.dummy.dummyCollections
import dev.jdtech.jellyfin.models.CollectionType
import dev.jdtech.jellyfin.models.FindroidItem
import dev.jdtech.jellyfin.presentation.theme.FindroidTheme
import dev.jdtech.jellyfin.presentation.theme.spacings

@Composable
fun LibraryCard(
    item: FindroidItem,
    onClick: (FindroidItem) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .height(64.dp)
            .fillMaxWidth()
            .clip(MaterialTheme.shapes.large)
            .clickable(
                onClick = {
                    onClick(item)
                },
            ),
    ) {
        ElevatedCard(
            shape = MaterialTheme.shapes.large,
            modifier = Modifier
                .fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceContainer
            )
        ) {
            Row(
                modifier = Modifier
                    .padding(horizontal = MaterialTheme.spacings.default)
                    .fillMaxHeight(),
                horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacings.small),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                val icon = when (item.type) {
                    CollectionType.Movies -> R.drawable.ic_movie
                    CollectionType.TvShows -> R.drawable.ic_tv
                    CollectionType.HomeVideos -> R.drawable.ic_video_library
                    CollectionType.Music -> R.drawable.ic_music_note
                    CollectionType.Playlists -> R.drawable.ic_playlist
                    CollectionType.Books -> R.drawable.ic_books
                    CollectionType.LiveTv -> R.drawable.ic_live_tv
                    CollectionType.BoxSets -> R.drawable.ic_video_library
                    CollectionType.Mixed -> R.drawable.ic_video_library
                    CollectionType.Unknown -> R.drawable.ic_video_library
                }
                Icon(
                    painter = painterResource(icon),
                    contentDescription = null,
                )
                Spacer(modifier = Modifier.width(MaterialTheme.spacings.extraSmall))
                Text(
                    text = item.name,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun LibraryCardPreview() {
    FindroidTheme {
        LibraryCard(
            item = dummyCollections[0],
            onClick = {},
        )
    }
}
