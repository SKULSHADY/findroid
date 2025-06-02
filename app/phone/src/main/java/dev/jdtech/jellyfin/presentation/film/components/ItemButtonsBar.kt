package dev.jdtech.jellyfin.presentation.film.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.jdtech.jellyfin.core.presentation.dummy.dummyEpisode
import dev.jdtech.jellyfin.models.FindroidItem
import dev.jdtech.jellyfin.models.FindroidMovie
import dev.jdtech.jellyfin.models.FindroidShow
import dev.jdtech.jellyfin.presentation.theme.FindroidTheme
import dev.jdtech.jellyfin.presentation.theme.spacings
import dev.jdtech.jellyfin.core.R as CoreR

@Composable
fun ItemButtonsBar(
    item: FindroidItem,
    onPlayClick: (startFromBeginning: Boolean) -> Unit,
    onMarkAsPlayedClick: () -> Unit,
    onMarkAsFavoriteClick: () -> Unit,
    onDownloadClick: () -> Unit,
    onTrailerClick: (uri: String) -> Unit,
    modifier: Modifier = Modifier,
    isLoadingPlayer: Boolean = false,
    isLoadingRestartPlayer: Boolean = false,
) {
    val trailerUri = when (item) {
        is FindroidMovie -> {
            item.trailer
        }

        is FindroidShow -> {
            item.trailer
        }

        else -> null
    }

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        PlayButton(
            item = item,
            onClick = {
                onPlayClick(false)
            },
            enabled = !isLoadingPlayer && !isLoadingRestartPlayer,
            isLoading = isLoadingPlayer,
        )
        if (item.playbackPositionTicks.div(600000000) > 0) {
            Spacer(Modifier.height(MaterialTheme.spacings.medium))
            FilledTonalButton(
                onClick = {
                    onPlayClick(true)
                },
                enabled = !isLoadingPlayer && !isLoadingRestartPlayer,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
            ) {
                when (isLoadingRestartPlayer) {
                    true -> {
                        CircularProgressIndicator(
                            modifier = Modifier.size(24.dp),
                            color = LocalContentColor.current,
                        )
                    }

                    false -> {
                        Icon(
                            painter = painterResource(CoreR.drawable.ic_rotate_ccw),
                            contentDescription = null,
                            modifier = Modifier.size(24.dp),
                        )
                    }
                }
                Spacer(modifier = Modifier.width(MaterialTheme.spacings.small))
                Text(
                    text = stringResource(CoreR.string.restart_playback),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                )
            }
        }
        Spacer(Modifier.height(MaterialTheme.spacings.default))
        Row(
            modifier = modifier
                .fillMaxWidth()
                .align(Alignment.CenterHorizontally),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(
                MaterialTheme.spacings.medium,
                Alignment.CenterHorizontally
            )
        ) {
            trailerUri?.let { uri ->
                FilledTonalIconButton(
                    onClick = {
                        onTrailerClick(uri)
                    },
                    modifier = Modifier
                        .size(48.dp)
                ) {
                    Icon(
                        painter = painterResource(CoreR.drawable.ic_film),
                        contentDescription = null,
                    )
                }
            }
            FilledTonalIconButton(
                onClick = onMarkAsPlayedClick,
                modifier = Modifier
                    .alpha(if (item.played) 1f else 0.4f)
                    .size(48.dp)
            ) {
                Icon(
                    painter = painterResource(CoreR.drawable.ic_check),
                    contentDescription = null,
                )
            }
            FilledTonalIconButton(
                onClick = onMarkAsFavoriteClick,
                modifier = Modifier
                    .alpha(if (item.favorite) 1f else 0.4f)
                    .size(48.dp)
            ) {
                Icon(
                    painter = painterResource(if (item.favorite) CoreR.drawable.ic_heart_filled else CoreR.drawable.ic_heart),
                    contentDescription = null,
                )
            }
            if (item.canDownload) {
                FilledTonalIconButton(
                    onClick = onDownloadClick,
                    enabled = false,
                    modifier = Modifier.size(48.dp)
                ) {
                    Icon(
                        painter = painterResource(CoreR.drawable.ic_download),
                        contentDescription = null,
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ItemButtonsBarPreview() {
    FindroidTheme {
        ItemButtonsBar(
            item = dummyEpisode,
            onPlayClick = {},
            onMarkAsPlayedClick = {},
            onMarkAsFavoriteClick = {},
            onDownloadClick = {},
            onTrailerClick = {},
        )
    }
}
