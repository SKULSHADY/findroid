package dev.jdtech.jellyfin.film.presentation.season

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.jdtech.jellyfin.models.EpisodeItem
import dev.jdtech.jellyfin.models.FindroidEpisode
import dev.jdtech.jellyfin.models.FindroidPerson
import dev.jdtech.jellyfin.models.FindroidSeason
import dev.jdtech.jellyfin.models.FindroidShow
import dev.jdtech.jellyfin.repository.JellyfinRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.jellyfin.sdk.model.api.ItemFields
import org.jellyfin.sdk.model.api.PersonKind
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class SeasonViewModel
@Inject
constructor(
    private val jellyfinRepository: JellyfinRepository,
) : ViewModel() {
    private val _uiState = MutableStateFlow<UiState>(UiState.Loading)
    val uiState = _uiState.asStateFlow()
    private val _state = MutableStateFlow(SeasonState())
    val state = _state.asStateFlow()

    private val eventsChannel = Channel<SeasonEvent>()
    val eventsChannelFlow = eventsChannel.receiveAsFlow()
    private var currentUiState: UiState = UiState.Loading

    sealed class UiState {
        data class Normal(
            val item: FindroidShow,
            val actors: List<FindroidPerson>,
            val season: FindroidSeason,
            val episodes: List<EpisodeItem>,
        ) : UiState()

        data object Loading : UiState()
        data class Error(val error: Exception) : UiState()
    }

    private lateinit var series: FindroidShow
    private lateinit var season: FindroidSeason

    fun loadData(seriesId: UUID, seasonId: UUID, offline: Boolean) {
        viewModelScope.launch {
            _uiState.emit(UiState.Loading)
            try {
                series = jellyfinRepository.getShow(seriesId)
                season = getSeason(seasonId)
                val episodes = getEpisodes(seriesId, seasonId, offline).map {
                    when (it) {
                        is FindroidEpisode -> EpisodeItem.Episode(it)
                        is EpisodeItem.Header -> it
                        else -> throw IllegalArgumentException("Unsupported type")
                    }
                }
                currentUiState = UiState.Normal(
                    item = series,
                    actors = getActors(item = series),
                    season = season,
                    episodes = episodes,
                )
                _uiState.emit(currentUiState)
                _state.emit(
                    _state.value.copy(
                        actors = getActors(item = series),
                        series = series,
                        season = season,
                        episodes = jellyfinRepository.getEpisodes(
                            seriesId,
                            seasonId,
                            fields = listOf(ItemFields.OVERVIEW),
                            offline = offline
                        ),
                    )
                )
            } catch (_: NullPointerException) {
                // Navigate back because item does not exist (probably because it's been deleted)
                eventsChannel.send(SeasonEvent.NavigateBack)
            } catch (e: Exception) {
                _uiState.emit(UiState.Error(e))
            }
        }
    }

    private suspend fun getSeason(seasonId: UUID): FindroidSeason {
        return jellyfinRepository.getSeason(seasonId)
    }

    private suspend fun getEpisodes(
        seriesId: UUID,
        seasonId: UUID,
        offline: Boolean
    ): List<Any> {
        val header = EpisodeItem.Header(
            seriesId = season.seriesId,
            seasonId = season.id,
            seriesName = season.seriesName,
            seasonName = season.name
        )
        val episodes =
            jellyfinRepository.getEpisodes(
                seriesId,
                seasonId,
                fields = listOf(ItemFields.OVERVIEW),
                offline = offline
            )

        return listOf(header) + episodes
    }

    private suspend fun getActors(item: FindroidShow): List<FindroidPerson> {
        val actors: List<FindroidPerson>
        withContext(Dispatchers.Default) {
            actors = item.people.filter { it.type == PersonKind.ACTOR }
        }
        return actors
    }

    private suspend fun getNextUp(seriesId: UUID): FindroidEpisode? {
        val nextUpItems = jellyfinRepository.getNextUp(seriesId)
        return nextUpItems.getOrNull(0)
    }

    private fun togglePlayed() {
        suspend fun updateUiPlayedState(played: Boolean) {
            season = season.copy(played = played)
            when (currentUiState) {
                is UiState.Normal -> {
                    currentUiState =
                        (currentUiState as UiState.Normal).copy(
                            season = season
                        )
                    _uiState.emit(currentUiState)
                }

                else -> {}
            }
        }

        viewModelScope.launch {
            val originalPlayedState = season.played
            updateUiPlayedState(!season.played)

            when (season.played) {
                false -> {
                    try {
                        jellyfinRepository.markAsUnplayed(season.id)
                    } catch (_: Exception) {
                        updateUiPlayedState(originalPlayedState)
                    }
                }

                true -> {
                    try {
                        jellyfinRepository.markAsPlayed(season.id)
                    } catch (_: Exception) {
                        updateUiPlayedState(originalPlayedState)
                    }
                }
            }
        }
    }

    private fun toggleFavorite() {
        suspend fun updateUiFavoriteState(isFavorite: Boolean) {
            season = season.copy(favorite = isFavorite)
            when (currentUiState) {
                is UiState.Normal -> {
                    currentUiState =
                        (currentUiState as UiState.Normal).copy(
                            season = season
                        )
                    _uiState.emit(currentUiState)
                }

                else -> {}
            }
        }

        viewModelScope.launch {
            val originalFavoriteState = season.favorite
            updateUiFavoriteState(!season.favorite)

            when (season.favorite) {
                false -> {
                    try {
                        jellyfinRepository.unmarkAsFavorite(season.id)
                    } catch (_: Exception) {
                        updateUiFavoriteState(originalFavoriteState)
                    }
                }

                true -> {
                    try {
                        jellyfinRepository.markAsFavorite(season.id)
                    } catch (_: Exception) {
                        updateUiFavoriteState(originalFavoriteState)
                    }
                }
            }
        }
    }

    fun onAction(action: SeasonAction, seriesId: UUID, seasonId: UUID) {
        when (action) {
            is SeasonAction.MarkAsPlayed, is SeasonAction.UnmarkAsPlayed -> {
                viewModelScope.launch {
                    togglePlayed()
                    loadData(seriesId, seasonId, false)
                }
            }

            is SeasonAction.MarkAsFavorite, SeasonAction.UnmarkAsFavorite -> {
                viewModelScope.launch {
                    toggleFavorite()
                    loadData(seriesId, seasonId, false)
                }
            }

            else -> Unit
        }
    }
}

sealed interface SeasonEvent {
    data object NavigateBack : SeasonEvent
}
