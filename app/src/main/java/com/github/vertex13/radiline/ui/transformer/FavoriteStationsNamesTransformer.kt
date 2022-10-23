package com.github.vertex13.radiline.ui.transformer

import com.github.vertex13.radiline.domain.FavoriteStations
import com.github.vertex13.radiline.domain.map
import com.github.vertex13.radiline.ui.FavoriteStationsNames

fun transformFavoriteStationNames(
    favoriteStations: FavoriteStations
): FavoriteStationsNames {
    return favoriteStations.map { stations ->
        stations.asSequence().map { it.name }.toHashSet()
    }
}
