package dev.jdtech.jellyfin.presentation.film.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.jdtech.jellyfin.film.presentation.home.HomeAction
import dev.jdtech.jellyfin.models.HomeSection
import dev.jdtech.jellyfin.presentation.theme.spacings

@Composable
fun HomeSection(
    section: HomeSection,
    itemsPadding: PaddingValues,
    onAction: (HomeAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
    ) {
        SectionHeader(text = section.name.asString())
        LazyRow(
            contentPadding = itemsPadding,
            horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacings.medium),
        ) {
            items(section.items, key = { it.id }) { item ->
                ItemCard(
                    item = item,
                    direction = Direction.HORIZONTAL,
                    onClick = {
                        onAction(HomeAction.OnItemClick(item))
                    },
                    forceThumbnail = true,
                )
            }
        }
    }
}
