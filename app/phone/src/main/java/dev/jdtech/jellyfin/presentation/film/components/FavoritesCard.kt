package dev.jdtech.jellyfin.presentation.film.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.jdtech.jellyfin.presentation.theme.FindroidTheme
import dev.jdtech.jellyfin.presentation.theme.spacings
import dev.jdtech.jellyfin.core.R as CoreR

@Composable
fun FavoritesCard(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    ElevatedCard(
        shape = MaterialTheme.shapes.large,
        modifier = Modifier
            .height(64.dp)
            .fillMaxWidth()
            .clip(MaterialTheme.shapes.large),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainer
        )
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = MaterialTheme.spacings.default)
                .fillMaxHeight(),
            horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacings.small),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                painter = painterResource(CoreR.drawable.ic_heart_filled),
                contentDescription = null,
            )
            Spacer(modifier = Modifier.width(MaterialTheme.spacings.extraSmall))
            Text(
                text = stringResource(CoreR.string.title_favorite),
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Preview
@Composable
private fun FavoritesCardPreview() {
    FindroidTheme {
        FavoritesCard(
            onClick = {},
            modifier = Modifier.width(320.dp),
        )
    }
}
