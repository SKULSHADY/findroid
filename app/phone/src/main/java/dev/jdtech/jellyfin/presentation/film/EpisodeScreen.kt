package dev.jdtech.jellyfin.presentation.film

import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.jdtech.jellyfin.PlayerActivity
import dev.jdtech.jellyfin.core.presentation.dummy.dummyEpisode
import dev.jdtech.jellyfin.core.presentation.dummy.dummyVideoMetadata
import dev.jdtech.jellyfin.film.presentation.episode.EpisodeAction
import dev.jdtech.jellyfin.film.presentation.episode.EpisodeState
import dev.jdtech.jellyfin.film.presentation.episode.EpisodeViewModel
import dev.jdtech.jellyfin.models.FindroidItem
import dev.jdtech.jellyfin.presentation.film.components.ActorsRow
import dev.jdtech.jellyfin.presentation.film.components.InfoBar
import dev.jdtech.jellyfin.presentation.film.components.ItemButtonsBar
import dev.jdtech.jellyfin.presentation.film.components.ItemHeader
import dev.jdtech.jellyfin.presentation.film.components.ScreenHeader
import dev.jdtech.jellyfin.presentation.film.components.VideoMetadataBar
import dev.jdtech.jellyfin.presentation.theme.FindroidTheme
import dev.jdtech.jellyfin.presentation.theme.spacings
import dev.jdtech.jellyfin.presentation.utils.rememberSafePadding
import dev.jdtech.jellyfin.utils.ObserveAsEvents
import dev.jdtech.jellyfin.viewmodels.PlayerItemsEvent
import dev.jdtech.jellyfin.viewmodels.PlayerViewModel
import java.util.UUID
import dev.jdtech.jellyfin.core.R as CoreR

@Composable
fun EpisodeScreen(
    episodeId: UUID,
    navigateBack: () -> Unit,
    viewModel: EpisodeViewModel = hiltViewModel(),
    playerViewModel: PlayerViewModel = hiltViewModel(),
    navigateToItem: (item: FindroidItem) -> Unit,
) {
    val context = LocalContext.current
    val state by viewModel.state.collectAsStateWithLifecycle()

    var isLoadingPlayer by remember { mutableStateOf(false) }
    var isLoadingRestartPlayer by remember { mutableStateOf(false) }

    LaunchedEffect(true) {
        viewModel.loadEpisode(episodeId = episodeId)
    }

    ObserveAsEvents(playerViewModel.eventsChannelFlow) { event ->
        when (event) {
            is PlayerItemsEvent.PlayerItemsReady -> {
                isLoadingPlayer = false
                isLoadingRestartPlayer = false
                val intent = Intent(context, PlayerActivity::class.java)
                intent.putExtra("items", ArrayList(event.items))
                context.startActivity(intent)
            }

            is PlayerItemsEvent.PlayerItemsError -> {
                isLoadingPlayer = false
                isLoadingRestartPlayer = false
                Toast.makeText(
                    context,
                    CoreR.string.error_preparing_player_items,
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    EpisodeScreenLayout(
        state = state,
        isLoadingPlayer = isLoadingPlayer,
        isLoadingRestartPlayer = isLoadingRestartPlayer,
        onAction = { action ->
            when (action) {
                is EpisodeAction.Play -> {
                    when (action.startFromBeginning) {
                        true -> isLoadingRestartPlayer = true
                        false -> isLoadingPlayer = true
                    }
                    state.episode?.let { episode ->
                        playerViewModel.loadPlayerItems(
                            episode,
                            startFromBeginning = action.startFromBeginning
                        )
                    }
                }

                is EpisodeAction.OnBackClick -> navigateBack()
                else -> Unit
            }
            viewModel.onAction(action)
        },
        navigateToItem = navigateToItem
    )
}

@Composable
private fun EpisodeScreenLayout(
    state: EpisodeState,
    isLoadingPlayer: Boolean,
    isLoadingRestartPlayer: Boolean,
    onAction: (EpisodeAction) -> Unit,
    navigateToItem: (item: FindroidItem) -> Unit,
) {
    val safePadding = rememberSafePadding()

    val paddingStart = safePadding.start + MaterialTheme.spacings.default
    val paddingEnd = safePadding.end + MaterialTheme.spacings.default
    val paddingBottom = safePadding.bottom + MaterialTheme.spacings.large

    val scrollState = rememberScrollState()

    var expandedOverview by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier.fillMaxSize(),
    ) {
        state.episode?.let { episode ->
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(scrollState),
            ) {
                ItemHeader(
                    item = episode,
                    scrollState = scrollState,
                    navigateToItem = { state.season?.let { season -> navigateToItem(season) } },
                )
                Column(
                    modifier = Modifier.padding(
                        start = paddingStart,
                        end = paddingEnd,
                    ),
                ) {
                    Text(
                        modifier = Modifier.align(Alignment.CenterHorizontally),
                        text = stringResource(
                            id = CoreR.string.season_episode,
                            episode.parentIndexNumber,
                            episode.indexNumber,
                            episode.name,
                        ),
                        overflow = TextOverflow.Ellipsis,
                        maxLines = 1,
                        style = MaterialTheme.typography.titleMedium,
                    )
                    InfoBar(item = episode)
                    Spacer(Modifier.height(MaterialTheme.spacings.medium))
                    state.videoMetadata?.let { videoMetadata ->
                        VideoMetadataBar(videoMetadata)
                        Spacer(Modifier.height(MaterialTheme.spacings.large))
                    }
                    ItemButtonsBar(
                        item = episode,
                        onPlayClick = { startFromBeginning ->
                            onAction(EpisodeAction.Play(startFromBeginning = startFromBeginning))
                        },
                        onMarkAsPlayedClick = {
                            when (episode.played) {
                                true -> onAction(EpisodeAction.UnmarkAsPlayed)
                                false -> onAction(EpisodeAction.MarkAsPlayed)
                            }
                        },
                        onMarkAsFavoriteClick = {
                            when (episode.favorite) {
                                true -> onAction(EpisodeAction.UnmarkAsFavorite)
                                false -> onAction(EpisodeAction.MarkAsFavorite)
                            }
                        },
                        onTrailerClick = {},
                        onDownloadClick = {},
                        modifier = Modifier.fillMaxWidth(),
                        isLoadingPlayer = isLoadingPlayer,
                        isLoadingRestartPlayer = isLoadingRestartPlayer,
                    )
                    Spacer(Modifier.height(MaterialTheme.spacings.large))
                    Text(
                        text = episode.overview,
                        modifier = Modifier
                            .clickable {
                                expandedOverview = !expandedOverview
                            },
                        overflow = TextOverflow.Ellipsis,
                        maxLines = if (expandedOverview) Int.MAX_VALUE else 3,
                        style = MaterialTheme.typography.bodyMedium,
                    )
                    Spacer(Modifier.height(MaterialTheme.spacings.large))
                }
                if (state.actors.isNotEmpty()) {
                    ActorsRow(
                        actors = state.actors,
                        contentPadding = PaddingValues(
                            start = paddingStart,
                            end = paddingEnd,
                        ),
                    )
                }
                Spacer(Modifier.height(paddingBottom))
            }
        } ?: run {
            CircularProgressIndicator(
                modifier = Modifier
                    .align(Alignment.Center),
            )
        }

        ScreenHeader(
            onAction = { onAction(EpisodeAction.OnBackClick) },
        )
    }
}

@PreviewScreenSizes
@Composable
private fun EpisodeScreenLayoutPreview() {
    FindroidTheme {
        EpisodeScreenLayout(
            state = EpisodeState(
                episode = dummyEpisode,
                videoMetadata = dummyVideoMetadata,
            ),
            isLoadingPlayer = false,
            isLoadingRestartPlayer = false,
            onAction = {},
            navigateToItem = {},
        )
    }
}
