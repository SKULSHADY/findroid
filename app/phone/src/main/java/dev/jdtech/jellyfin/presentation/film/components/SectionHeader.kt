package dev.jdtech.jellyfin.presentation.film.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.jdtech.jellyfin.core.R
import dev.jdtech.jellyfin.presentation.theme.spacings

@Composable
fun SectionHeader(
    text: String,
    action: (() -> Unit)? = null
) {
    val density = LocalDensity.current
    val layoutDirection = LocalLayoutDirection.current

    val safePaddingStart =
        with(density) { WindowInsets.safeDrawing.getLeft(this, layoutDirection).toDp() }
    val safePaddingEnd =
        with(density) { WindowInsets.safeDrawing.getRight(this, layoutDirection).toDp() }
    val paddingStart = safePaddingStart + MaterialTheme.spacings.default + 2.dp
    val paddingEnd = safePaddingEnd + MaterialTheme.spacings.default

    Row(
        modifier = Modifier
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ) {
                if (action != null) {
                    action()
                }
            }
            .height(48.dp)
            .fillMaxWidth()
            .padding(bottom = MaterialTheme.spacings.small),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column {
            Text(
                text = text,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Medium,
                fontSize = 16.sp,
                modifier = Modifier
                    .padding(
                        start = paddingStart,
                        end = paddingEnd,
                    ),
                textAlign = TextAlign.Center,
            )
        }
        action?.let {
            Spacer(Modifier.weight(1f))
            Icon(
                painter = painterResource(R.drawable.ic_next),
                contentDescription = null,
                modifier = Modifier
                    .padding(
                        start = paddingStart,
                        end = MaterialTheme.spacings.medium,
                    ),
            )
        }
    }
}