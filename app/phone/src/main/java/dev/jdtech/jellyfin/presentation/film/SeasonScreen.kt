package dev.jdtech.jellyfin.presentation.film

import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.jdtech.jellyfin.PlayerActivity
import dev.jdtech.jellyfin.core.presentation.dummy.dummyShow
import dev.jdtech.jellyfin.film.presentation.season.SeasonAction
import dev.jdtech.jellyfin.film.presentation.season.SeasonState
import dev.jdtech.jellyfin.film.presentation.season.SeasonViewModel
import dev.jdtech.jellyfin.models.FindroidItem
import dev.jdtech.jellyfin.presentation.film.components.Direction
import dev.jdtech.jellyfin.presentation.film.components.InfoBar
import dev.jdtech.jellyfin.presentation.film.components.ItemButtonsBar
import dev.jdtech.jellyfin.presentation.film.components.ItemCard
import dev.jdtech.jellyfin.presentation.film.components.ItemHeader
import dev.jdtech.jellyfin.presentation.film.components.PersonItem
import dev.jdtech.jellyfin.presentation.film.components.ScreenHeader
import dev.jdtech.jellyfin.presentation.film.components.SectionHeader
import dev.jdtech.jellyfin.presentation.theme.FindroidTheme
import dev.jdtech.jellyfin.presentation.theme.spacings
import dev.jdtech.jellyfin.presentation.utils.rememberSafePadding
import dev.jdtech.jellyfin.utils.ObserveAsEvents
import dev.jdtech.jellyfin.viewmodels.PlayerItemsEvent
import dev.jdtech.jellyfin.viewmodels.PlayerViewModel
import java.util.UUID
import dev.jdtech.jellyfin.core.R as CoreR

@Composable
fun SeasonScreen(
    showId: UUID,
    seasonId: UUID,
    navigateBack: () -> Unit,
    viewModel: SeasonViewModel = hiltViewModel(),
    playerViewModel: PlayerViewModel = hiltViewModel(),
    navigateToItem: (item: FindroidItem) -> Unit,
) {
    val context = LocalContext.current
    val state by viewModel.state.collectAsStateWithLifecycle()

    var isLoadingPlayer by remember { mutableStateOf(false) }
    var isLoadingRestartPlayer by remember { mutableStateOf(false) }

    LaunchedEffect(true) {
        viewModel.loadData(seriesId = showId, seasonId = seasonId, offline = false)
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

    SeasonScreenLayout(
        state = state,
        isLoadingPlayer = isLoadingPlayer,
        isLoadingRestartPlayer = isLoadingRestartPlayer,
        onAction = { action ->
            when (action) {
                is SeasonAction.Play -> {
                    when (action.startFromBeginning) {
                        true -> isLoadingRestartPlayer = true
                        false -> isLoadingPlayer = true
                    }
                    state.season?.let { season ->
                        playerViewModel.loadPlayerItems(
                            season,
                            startFromBeginning = action.startFromBeginning
                        )
                    }
                }

                is SeasonAction.OnBackClick -> navigateBack()
                else -> Unit
            }
            viewModel.onAction(action, showId, seasonId)
        },
        navigateToItem = navigateToItem
    )
}

@Composable
private fun SeasonScreenLayout(
    state: SeasonState,
    isLoadingPlayer: Boolean,
    isLoadingRestartPlayer: Boolean,
    onAction: (SeasonAction) -> Unit,
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
        state.also { state ->
            val series = state.series
            val season = state.season
            if (series != null && season != null) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .verticalScroll(scrollState),
                ) {
                    ItemHeader(
                        item = series,
                        scrollState = scrollState,
                        navigateToItem = { navigateToItem(series) },
                    )
                    Column(
                        modifier = Modifier.padding(
                            start = paddingStart,
                            end = paddingEnd,
                        ),
                    ) {
                        InfoBar(item = series)
                        Spacer(Modifier.height(MaterialTheme.spacings.large))
                        ItemButtonsBar(
                            item = season,
                            onPlayClick = { startFromBeginning ->
                                onAction(SeasonAction.Play(startFromBeginning = startFromBeginning))
                            },
                            onMarkAsPlayedClick = {
                                when (season.played) {
                                    true -> onAction(SeasonAction.UnmarkAsPlayed)
                                    false -> onAction(SeasonAction.MarkAsPlayed)
                                }
                            },
                            onMarkAsFavoriteClick = {
                                when (season.favorite) {
                                    true -> onAction(SeasonAction.UnmarkAsFavorite)
                                    false -> onAction(SeasonAction.MarkAsFavorite)
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
                            text = season.overview.ifEmpty { series.overview },
                            modifier = Modifier
                                .clickable {
                                    expandedOverview = !expandedOverview
                                },
                            overflow = TextOverflow.Ellipsis,
                            maxLines = if (expandedOverview) Int.MAX_VALUE else 3,
                            style = MaterialTheme.typography.bodyMedium,
                        )
                        Spacer(Modifier.height(MaterialTheme.spacings.medium))
                        Column(
                            verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacings.small),
                        ) {
                            Text(
                                text = "${stringResource(CoreR.string.genres)}: ${series.genres.joinToString()}",
                                style = MaterialTheme.typography.bodyMedium,
                            )
                        }
                        Spacer(Modifier.height(MaterialTheme.spacings.large))
                    }
                    if (state.episodes.isNotEmpty()) {
                        SectionHeader(season.name)
                        LazyRow(
                            contentPadding = PaddingValues(
                                start = paddingStart,
                                end = paddingEnd,
                            ),
                            horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacings.medium),
                        ) {
                            items(
                                items = state.episodes,
                                key = { episodes ->
                                    episodes.id
                                },
                            ) { episode ->
                                ItemCard(
                                    item = episode,
                                    direction = Direction.HORIZONTAL,
                                    onClick = { navigateToItem(episode) },
                                    modifier = Modifier,
                                )
                            }
                        }
                        Spacer(Modifier.height(MaterialTheme.spacings.large))
                    }
                    if (state.actors.isNotEmpty()) {
                        SectionHeader(stringResource(CoreR.string.cast_amp_crew))
                        LazyRow(
                            contentPadding = PaddingValues(
                                start = paddingStart,
                                end = paddingEnd,
                            ),
                            horizontalArrangement = Arrangement.spacedBy(
                                MaterialTheme.spacings.medium,
                                Alignment.CenterHorizontally
                            ),
                        ) {
                            items(
                                items = state.actors,
                                key = { person ->
                                    person.id
                                },
                            ) { person ->
                                PersonItem(
                                    person = person,
                                )
                            }
                        }
                    }
                    Spacer(Modifier.height(paddingBottom))
                }
            }

            ScreenHeader(onAction = { onAction(SeasonAction.OnBackClick) })
        }
    }
}

@PreviewScreenSizes
@Composable
private fun EpisodeScreenLayoutPreview() {
    FindroidTheme {
        SeasonScreenLayout(
            state = SeasonState(
                season = dummyShow.seasons[0],
            ),
            isLoadingPlayer = false,
            isLoadingRestartPlayer = false,
            onAction = {},
            navigateToItem = {},
        )
    }
}
