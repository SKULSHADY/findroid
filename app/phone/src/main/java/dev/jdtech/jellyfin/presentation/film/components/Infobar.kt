package dev.jdtech.jellyfin.presentation.film.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import dev.jdtech.jellyfin.models.FindroidEpisode
import dev.jdtech.jellyfin.models.FindroidItem
import dev.jdtech.jellyfin.models.FindroidShow
import dev.jdtech.jellyfin.presentation.theme.spacings
import dev.jdtech.jellyfin.utils.format
import dev.jdtech.jellyfin.utils.getShowDateString
import dev.jdtech.jellyfin.core.R as CoreR

@Composable
fun InfoBar(
    item: FindroidItem,
) {
    Spacer(Modifier.height(MaterialTheme.spacings.small))
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(
            12.dp,
            Alignment.CenterHorizontally
        ),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        if (item is FindroidShow) {
            item.productionYear?.let {
                Text(
                    text = getShowDateString(item),
                    style = MaterialTheme.typography.titleMedium,
                )
            }
        } else {
            item.premiereDate?.let { premiereDate ->
                Text(
                    text = when (item) {
                        is FindroidEpisode -> premiereDate.format()
                        else -> premiereDate.year.toString()
                    },
                    style = MaterialTheme.typography.titleMedium,
                )
            }
        }
        if (item.runtimeTicks > 0) {
            val hours = item.runtimeTicks.div(36000000000)
            val minutes =
                if (hours > 0) item.runtimeTicks.div(60000000).mod(60) else item.runtimeTicks.div(
                    600000000
                )
            Text(
                text = if (hours > 0) "${hours}h ${minutes}m" else "$minutes mins",
                style = MaterialTheme.typography.titleMedium,
            )
        }
        item.officialRating?.let { officialRating ->
            Text(
                text = officialRating,
                style = MaterialTheme.typography.titleSmall,
                modifier = Modifier
                    .clip(MaterialTheme.shapes.extraSmall)
                    .background(MaterialTheme.colorScheme.surfaceContainerHigh)
                    .padding(
                        horizontal = MaterialTheme.spacings.small,
                        vertical = 2.dp,
                    ),
            )
        }
        item.communityRating?.let { communityRating ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Icon(
                    painter = painterResource(CoreR.drawable.ic_imdb),
                    contentDescription = null,
                    tint = Color.Unspecified,
                    modifier = Modifier.height(16.dp)
                )
                Spacer(Modifier.width(MaterialTheme.spacings.small))
                Text(
                    text = "%.1f".format(communityRating),
                    style = MaterialTheme.typography.titleMedium,
                )
            }
        }
        item.criticRating?.let { criticRating ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Icon(
                    painter = painterResource(if (criticRating > 59f) CoreR.drawable.ic_rt_fresh else CoreR.drawable.ic_rt_rotten),
                    contentDescription = null,
                    tint = Color.Unspecified,
                    modifier = Modifier.height(16.dp)
                )
                Spacer(Modifier.width(MaterialTheme.spacings.small))
                Text(
                    text = "${criticRating.toInt()}%",
                    style = MaterialTheme.typography.titleMedium,
                )
            }
        }
    }
}
