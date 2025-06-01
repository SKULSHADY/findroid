package dev.jdtech.jellyfin.presentation.setup.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import dev.jdtech.jellyfin.presentation.theme.FindroidTheme
import dev.jdtech.jellyfin.presentation.theme.spacings
import dev.jdtech.jellyfin.core.R as CoreR

@Composable
fun HeaderButton(painter: Painter, modifier: Modifier = Modifier, onAction: () -> Unit) {
    IconButton(
        onClick = { onAction() },
        modifier = modifier
            .padding(horizontal = MaterialTheme.spacings.default)
            .background(
                MaterialTheme.colorScheme.surfaceContainer,
                MaterialTheme.shapes.extraLarge
            ),
    ) {
        Icon(
            painter = painter,
            contentDescription = null
        )
    }
}

@Composable
@Preview
private fun HeaderButtonPreview() {
    FindroidTheme {
        HeaderButton(
            painter = painterResource(CoreR.drawable.ic_arrow_left),
            modifier = Modifier,
            onAction = {},
        )
    }
}
