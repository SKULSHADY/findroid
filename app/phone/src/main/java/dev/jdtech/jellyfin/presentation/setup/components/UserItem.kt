package dev.jdtech.jellyfin.presentation.setup.components

import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.jdtech.jellyfin.presentation.theme.FindroidTheme
import dev.jdtech.jellyfin.presentation.theme.spacings
import dev.jdtech.jellyfin.core.R as CoreR

@Composable
fun UserItem(
    name: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
    onLongClick: () -> Unit = {}
) {
    val haptics = LocalHapticFeedback.current

    Row(
        horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacings.medium),
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .background(MaterialTheme.colorScheme.surfaceContainer, MaterialTheme.shapes.extraLarge)
            .fillMaxWidth()
            .clip(MaterialTheme.shapes.extraLarge)
            .combinedClickable(
                onClick = onClick,
                onLongClick = {
                    haptics.performHapticFeedback(HapticFeedbackType.LongPress)
                    onLongClick()
                },
            )
            .padding(MaterialTheme.spacings.medium),
    ) {
        Surface(
            color = MaterialTheme.colorScheme.surfaceTint,
            shape = CircleShape,
            modifier = Modifier
                .size(48.dp),
        ) {
            Box {
                Icon(
                    painter = painterResource(CoreR.drawable.ic_user),
                    contentDescription = null,
                    modifier = Modifier.align(Alignment.Center),
                )
            }
        }
        Text(
            text = name,
            style = MaterialTheme.typography.titleMedium,
        )
    }
}

@Composable
@Preview(showBackground = true)
private fun UserItemPreview() {
    FindroidTheme {
        UserItem(
            name = "Bob",
            modifier = Modifier.width(240.dp),
        )
    }
}
