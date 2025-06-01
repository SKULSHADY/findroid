package dev.jdtech.jellyfin.presentation.setup.components

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import dev.jdtech.jellyfin.presentation.theme.FindroidTheme
import dev.jdtech.jellyfin.presentation.theme.spacings

@Composable
fun HeaderText(text: String) {
    Spacer(modifier = Modifier.height(MaterialTheme.spacings.large))
    Text(
        text = text,
        style = MaterialTheme.typography.titleMedium,
        modifier = Modifier.padding(start = MaterialTheme.spacings.small).fillMaxWidth()
    )
    Spacer(modifier = Modifier.height(MaterialTheme.spacings.medium))
}

@Composable
@Preview
private fun HeaderPreview() {
    FindroidTheme {
        HeaderText(
            text = "Servers",
        )
    }
}
