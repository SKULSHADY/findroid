package dev.jdtech.jellyfin.presentation.film.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.jdtech.jellyfin.core.presentation.dummy.dummyMovie
import dev.jdtech.jellyfin.models.FindroidItem
import dev.jdtech.jellyfin.presentation.theme.FindroidTheme

@Composable
fun ProgressBar(
    item: FindroidItem,
    modifier: Modifier = Modifier,
    width: Int? = null,
) {
    Column(
        modifier = modifier,
    ) {
        Box(
            modifier = if (width != null) {
                Modifier
                    .height(6.dp)
                    .width(
                        item.playbackPositionTicks
                            .div(
                                item.runtimeTicks.toFloat(),
                            )
                            .times(
                                width - 16,
                            ).dp,
                    )
                    .clip(
                        MaterialTheme.shapes.extraSmall,
                    )
                    .background(
                        MaterialTheme.colorScheme.primary,
                    )
            } else {
                Modifier
                    .height(if (item.playbackPositionTicks != 0L) 6.dp else 0.dp)
                    .fillMaxWidth()
                    .clip(
                        MaterialTheme.shapes.extraSmall,
                    )
                    .background(
                        MaterialTheme.colorScheme.surfaceVariant,
                    )
            }
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun ProgressBarPreview() {
    FindroidTheme {
        ProgressBar(
            item = dummyMovie,
            width = 142,
        )
    }
}
