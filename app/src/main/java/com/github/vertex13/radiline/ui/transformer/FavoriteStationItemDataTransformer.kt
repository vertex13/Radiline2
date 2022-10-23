package com.github.vertex13.radiline.ui.transformer

import com.github.vertex13.radiline.domain.FavoriteStations
import com.github.vertex13.radiline.domain.combineObs
import com.github.vertex13.radiline.ui.FavoriteStationsItemData
import com.github.vertex13.radiline.ui.PlayingStationName
import com.github.vertex13.radiline.ui.view.StationItemData

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
