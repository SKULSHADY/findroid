package dev.jdtech.jellyfin.presentation.setup.components

import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.jdtech.jellyfin.presentation.theme.FindroidTheme
import dev.jdtech.jellyfin.presentation.theme.spacings
import dev.jdtech.jellyfin.core.R as CoreR

@Composable
fun ServerItem(
    name: String,
    address: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
    onLongClick: () -> Unit = {}
) {
    val haptics = LocalHapticFeedback.current

    ElevatedCard(
        modifier = modifier
            .clip(MaterialTheme.shapes.extraLarge)
            .combinedClickable(
                onClick = onClick,
                onLongClick = {
                    haptics.performHapticFeedback(HapticFeedbackType.LongPress)
                    onLongClick()
                },
            ),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainer
        )
    ) {
        Row(
            modifier = Modifier.padding(MaterialTheme.spacings.medium),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(CoreR.drawable.ic_server),
                contentDescription = null,
                modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .background(
                        MaterialTheme.colorScheme.surfaceTint,
                        MaterialTheme.shapes.extraLarge
                    )
                    .padding(12.dp),
                tint = MaterialTheme.colorScheme.onPrimary
            )
            Spacer(modifier = Modifier.width(MaterialTheme.spacings.default))
            Column {
                Text(text = name, style = MaterialTheme.typography.titleMedium)
                Text(
                    text = address,
                    style = MaterialTheme.typography.bodyMedium,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

@Composable
@Preview
private fun ServerItemPreview() {
    FindroidTheme {
        ServerItem(
            name = "Jellyfin Server",
            address = "http://192.168.0.10:8096",
        )
    }
}
