package dev.jdtech.jellyfin.presentation.film.components

import android.annotation.SuppressLint
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import dev.jdtech.jellyfin.presentation.setup.components.HeaderButton
import dev.jdtech.jellyfin.presentation.utils.rememberSafePadding
import dev.jdtech.jellyfin.core.R as CoreR

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun ScreenHeader(
    onAction: (() -> Unit)? = null,
) {
    val backgroundColor = MaterialTheme.colorScheme.background
    Scaffold(
        modifier = Modifier
            .height(64.dp)
            .fillMaxWidth(),
        containerColor = Color.Transparent
    ) {
        Canvas(
            modifier = Modifier
                .height(64.dp)
                .fillMaxWidth(),
        ) {
            drawRect(
                brush = Brush.verticalGradient(
                    colors = listOf(backgroundColor.copy(0.8f), Color.Transparent),
                ),
            )
        }
    }

    onAction?.let {
        HeaderButton(
            painter = painterResource(CoreR.drawable.ic_arrow_left),
            onAction = it,
            modifier = Modifier
                .padding(top = rememberSafePadding().top)
                .alpha(0.7f),
        )
    }
}
