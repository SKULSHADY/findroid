package dev.jdtech.jellyfin.presentation.film.components

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.jdtech.jellyfin.core.presentation.dummy.dummyEpisode
import dev.jdtech.jellyfin.core.presentation.dummy.dummyMovie
import dev.jdtech.jellyfin.models.FindroidItem
import dev.jdtech.jellyfin.presentation.theme.FindroidTheme
import dev.jdtech.jellyfin.presentation.theme.spacings
import dev.jdtech.jellyfin.core.R as CoreR

@Composable
fun PlayButton(
    item: FindroidItem,
    onClick: () -> Unit,
    enabled: Boolean = true,
    isLoading: Boolean = false,
) {
    val runtimeMinutes by remember(item.playbackPositionTicks) {
        mutableLongStateOf(item.playbackPositionTicks.div(600000000))
    }
    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp),
        enabled = enabled,
    ) {
        when (isLoading) {
            true -> {
                CircularProgressIndicator(
                    modifier = Modifier.size(24.dp),
                    color = LocalContentColor.current,
                )
            }

            false -> {
                Icon(
                    painter = painterResource(CoreR.drawable.ic_play),
                    contentDescription = null,
                    modifier = Modifier.size(28.dp)
                )
            }
        }
        Spacer(modifier = Modifier.width(MaterialTheme.spacings.small))
        Text(
            text = if (runtimeMinutes > 0) stringResource(
                CoreR.string.resume_runtime_minutes,
                runtimeMinutes
            ) else stringResource(CoreR.string.play),
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun PlayButtonMoviePreview() {
    FindroidTheme {
        PlayButton(
            item = dummyMovie,
            onClick = {},
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun PlayButtonEpisodePreview() {
    FindroidTheme {
        PlayButton(
            item = dummyEpisode,
            onClick = {},
        )
    }
}
