package dev.jdtech.jellyfin.presentation.film.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import dev.jdtech.jellyfin.core.presentation.dummy.dummyPerson
import dev.jdtech.jellyfin.models.FindroidPerson
import dev.jdtech.jellyfin.presentation.theme.FindroidTheme
import dev.jdtech.jellyfin.presentation.theme.spacings
import dev.jdtech.jellyfin.core.R as CoreR

@Composable
fun PersonItem(
    person: FindroidPerson,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .width(104.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        AsyncImage(
            model = person.image.uri,
            contentDescription = null,
            alignment = Alignment.Center,
            modifier = Modifier
                .clip(CircleShape)
                .background(
                    MaterialTheme.colorScheme.surfaceContainer,
                )
                .fillMaxWidth()
                .height(104.dp),
            error = painterResource(CoreR.drawable.ic_person),
            contentScale = ContentScale.Crop,
        )
        Spacer(Modifier.height(MaterialTheme.spacings.extraSmall))
        Text(
            text = person.name,
            overflow = TextOverflow.Ellipsis,
            maxLines = 1,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.alpha(0.8f),
        )
        Text(
            text = person.role,
            overflow = TextOverflow.Ellipsis,
            maxLines = 1,
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier.alpha(0.8f),
        )
    }
}

@Composable
@Preview(showBackground = true)
private fun PersonItemPreview() {
    FindroidTheme {
        PersonItem(
            person = dummyPerson,
        )
    }
}
