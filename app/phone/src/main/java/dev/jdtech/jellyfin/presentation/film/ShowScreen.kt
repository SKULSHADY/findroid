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
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.jdtech.jellyfin.PlayerActivity
import dev.jdtech.jellyfin.core.presentation.dummy.dummyShow
import dev.jdtech.jellyfin.film.presentation.show.ShowAction
import dev.jdtech.jellyfin.film.presentation.show.ShowState
import dev.jdtech.jellyfin.film.presentation.show.ShowViewModel
import dev.jdtech.jellyfin.models.FindroidItem
import dev.jdtech.jellyfin.presentation.film.components.ActorsRow
import dev.jdtech.jellyfin.presentation.film.components.Direction
import dev.jdtech.jellyfin.presentation.film.components.InfoBar
import dev.jdtech.jellyfin.presentation.film.components.InfoText
import dev.jdtech.jellyfin.presentation.film.components.ItemButtonsBar
import dev.jdtech.jellyfin.presentation.film.components.ItemCard
import dev.jdtech.jellyfin.presentation.film.components.ItemHeader
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
fun ShowScreen(
    showId: UUID,
    navigateBack: () -> Unit,
    navigateToItem: (item: FindroidItem) -> Unit,
    viewModel: ShowViewModel = hiltViewModel(),
    playerViewModel: PlayerViewModel = hiltViewModel(),
) {
    val context = LocalContext.current
    val uriHandler = LocalUriHandler.current

    val state by viewModel.state.collectAsStateWithLifecycle()

    var isLoadingPlayer by remember { mutableStateOf(false) }
    var isLoadingRestartPlayer by remember { mutableStateOf(false) }

    LaunchedEffect(true) {
        viewModel.loadShow(showId = showId)
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

    ShowScreenLayout(
        state = state,
        isLoadingPlayer = isLoadingPlayer,
        isLoadingRestartPlayer = isLoadingRestartPlayer,
        onAction = { action ->
            when (action) {
                is ShowAction.Play -> {
                    when (action.startFromBeginning) {
                        true -> isLoadingRestartPlayer = true
                        false -> isLoadingPlayer = true
                    }
                    state.show?.let { show ->
                        playerViewModel.loadPlayerItems(
                            show,
                            startFromBeginning = action.startFromBeginning
                        )
                    }
                }

                is ShowAction.PlayTrailer -> {
                    try {
                        uriHandler.openUri(action.trailer)
                    } catch (e: IllegalArgumentException) {
                        Toast.makeText(context, e.localizedMessage, Toast.LENGTH_SHORT).show()
                    }
                }

                is ShowAction.OnBackClick -> navigateBack()
                is ShowAction.NavigateToItem -> navigateToItem(action.item)
                else -> Unit
            }
            viewModel.onAction(action)
        },
    )
}

@Composable
private fun ShowScreenLayout(
    state: ShowState,
    isLoadingPlayer: Boolean,
    isLoadingRestartPlayer: Boolean,
    onAction: (ShowAction) -> Unit,
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
        state.show?.let { show ->
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(scrollState),
            ) {
                ItemHeader(
                    item = show,
                    scrollState = scrollState,
                )
                Column(
                    modifier = Modifier
                        .padding(
                            start = paddingStart,
                            end = paddingEnd,
                        ),
                ) {
                    InfoBar(item = show)
                    Spacer(Modifier.height(MaterialTheme.spacings.large))
                    ItemButtonsBar(
                        item = show,
                        onPlayClick = { startFromBeginning ->
                            onAction(ShowAction.Play(startFromBeginning = startFromBeginning))
                        },
                        onMarkAsPlayedClick = {
                            when (show.played) {
                                true -> onAction(ShowAction.UnmarkAsPlayed)
                                false -> onAction(ShowAction.MarkAsPlayed)
                            }
                        },
                        onMarkAsFavoriteClick = {
                            when (show.favorite) {
                                true -> onAction(ShowAction.UnmarkAsFavorite)
                                false -> onAction(ShowAction.MarkAsFavorite)
                            }
                        },
                        onTrailerClick = { uri ->
                            onAction(ShowAction.PlayTrailer(uri))
                        },
                        onDownloadClick = {},
                        modifier = Modifier.fillMaxWidth(),
                        isLoadingPlayer = isLoadingPlayer,
                        isLoadingRestartPlayer = isLoadingRestartPlayer,
                    )
                    Spacer(Modifier.height(MaterialTheme.spacings.large))
                    Text(
                        text = show.overview,
                        modifier = Modifier
                            .clickable {
                                expandedOverview = !expandedOverview
                            },
                        overflow = TextOverflow.Ellipsis,
                        maxLines = if (expandedOverview) Int.MAX_VALUE else 3,
                        style = MaterialTheme.typography.bodyMedium,
                    )
                    Spacer(Modifier.height(MaterialTheme.spacings.medium))
                    InfoText(
                        genres = show.genres,
                        director = state.director,
                        writers = state.writers,
                    )
                    Spacer(Modifier.height(MaterialTheme.spacings.large))
                }
                state.nextUp?.let { nextUp ->
                    SectionHeader(
                        text = stringResource(CoreR.string.next_up),
                    )
                    LazyRow(
                        contentPadding = PaddingValues(
                            start = paddingStart,
                            end = paddingEnd,
                        ),
                        horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacings.medium),
                    ) {
                        item(
                            key = nextUp.id,
                        ) {
                            ItemCard(
                                item = nextUp,
                                direction = Direction.HORIZONTAL,
                                onClick = {
                                    onAction(ShowAction.NavigateToItem(nextUp))
                                },
                                modifier = Modifier,
                            )
                        }
                    }
                    Spacer(Modifier.height(MaterialTheme.spacings.large))
                }
                if (state.seasons.isNotEmpty()) {
                    SectionHeader(stringResource(CoreR.string.seasons))
                    LazyRow(
                        contentPadding = PaddingValues(
                            start = paddingStart,
                            end = paddingEnd,
                        ),
                        horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacings.medium),
                    ) {
                        items(
                            items = state.seasons,
                            key = { item ->
                                item.id
                            },
                        ) { season ->
                            ItemCard(
                                item = season,
                                direction = Direction.VERTICAL,
                                onClick = {
                                    onAction(ShowAction.NavigateToItem(season))
                                },
                                modifier = Modifier,
                            )
                        }
                    }
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
    }

    ScreenHeader(
        onAction = { onAction(ShowAction.OnBackClick) },
    )
}

@PreviewScreenSizes
@Composable
private fun EpisodeScreenLayoutPreview() {
    FindroidTheme {
        ShowScreenLayout(
            state = ShowState(
                show = dummyShow,
            ),
            isLoadingPlayer = false,
            isLoadingRestartPlayer = false,
            onAction = {},
        )
    }
}
