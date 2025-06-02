package dev.jdtech.jellyfin.presentation.film.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import dev.jdtech.jellyfin.film.presentation.home.HomeAction
import dev.jdtech.jellyfin.models.FindroidCollection
import dev.jdtech.jellyfin.models.FindroidImages
import dev.jdtech.jellyfin.models.HomeItem
import dev.jdtech.jellyfin.presentation.theme.spacings
import dev.jdtech.jellyfin.film.R as FilmR

@Composable
fun HomeView(
    view: HomeItem.ViewItem,
    itemsPadding: PaddingValues,
    onAction: (HomeAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
    ) {
        SectionHeader(
            text = stringResource(
                FilmR.string.latest_library, view.view.name
            ),
            action = {
                onAction(
                    HomeAction.OnLibraryClick(
                        FindroidCollection(
                            id = view.view.id,
                            name = view.view.name,
                            images = FindroidImages(),
                            type = view.view.type,
                        ),
                    ),
                )
            },
        )
        LazyRow(
            contentPadding = itemsPadding,
            horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacings.medium),
        ) {
            items(view.view.items, key = { it.id }) { item ->
                ItemCard(
                    item = item,
                    direction = Direction.VERTICAL,
                    onClick = {
                        onAction(HomeAction.OnItemClick(item))
                    },
                )
            }
        }
    }
}
