package xyz.skether.radiline.ui.transformer

import xyz.skether.radiline.domain.FavoriteStations
import xyz.skether.radiline.domain.map
import xyz.skether.radiline.ui.FavoriteStationsNames

fun transformFavoriteStationNames(
    favoriteStations: FavoriteStations
): FavoriteStationsNames {
    return favoriteStations.map { stations ->
        stations.asSequence().map { it.name }.toHashSet()
    }
}
