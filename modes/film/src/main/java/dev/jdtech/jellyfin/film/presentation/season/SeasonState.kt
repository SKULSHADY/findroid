package dev.jdtech.jellyfin.film.presentation.season

import dev.jdtech.jellyfin.models.FindroidEpisode
import dev.jdtech.jellyfin.models.FindroidPerson
import dev.jdtech.jellyfin.models.FindroidSeason
import dev.jdtech.jellyfin.models.FindroidShow

data class SeasonState(
    val series: FindroidShow? = null,
    val season: FindroidSeason? = null,
    val actors: List<FindroidPerson> = emptyList(),
    val episodes: List<FindroidEpisode> = emptyList(),
    val error: Exception? = null,
)
