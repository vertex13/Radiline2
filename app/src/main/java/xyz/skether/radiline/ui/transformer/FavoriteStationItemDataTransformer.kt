package xyz.skether.radiline.ui.transformer

import xyz.skether.radiline.domain.FavoriteStations
import xyz.skether.radiline.domain.combineObs
import xyz.skether.radiline.ui.FavoriteStationsItemData
import xyz.skether.radiline.ui.PlayingStationName
import xyz.skether.radiline.ui.view.StationItemData

fun transformFavoriteStationsItemData(
    favoriteStations: FavoriteStations,
    playingStationName: PlayingStationName,
): FavoriteStationsItemData {
    return combineObs(favoriteStations, playingStationName) { fav, playingName ->
        fav.map {
            StationItemData(it, it.name == playingName)
        }
    }
}
