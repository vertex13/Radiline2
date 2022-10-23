package com.github.vertex13.radiline.ui.transformer

import com.github.vertex13.radiline.domain.TopStations
import com.github.vertex13.radiline.domain.combineObs
import com.github.vertex13.radiline.ui.FavoriteStationsNames
import com.github.vertex13.radiline.ui.PlayingStationName
import com.github.vertex13.radiline.ui.TopStationItemDataWithoutFavorites
import com.github.vertex13.radiline.ui.view.StationItemData

fun transformTopStationsItemDataWithoutFavorites(
    topStations: TopStations,
    favoriteStationsNames: FavoriteStationsNames,
    playingStationName: PlayingStationName,
): TopStationItemDataWithoutFavorites {
    return combineObs(
        topStations, favoriteStationsNames, playingStationName
    ) { top, favNames, playingName ->
        top.asSequence()
            .filter { !favNames.contains(it.name) }
            .map { StationItemData(it, it.name == playingName) }
            .toList()
    }
}
