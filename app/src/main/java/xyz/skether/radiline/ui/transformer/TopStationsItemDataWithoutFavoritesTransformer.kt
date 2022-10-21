package xyz.skether.radiline.ui.transformer

import xyz.skether.radiline.domain.TopStations
import xyz.skether.radiline.domain.combineObs
import xyz.skether.radiline.ui.FavoriteStationsNames
import xyz.skether.radiline.ui.PlayingStationName
import xyz.skether.radiline.ui.TopStationItemDataWithoutFavorites
import xyz.skether.radiline.ui.view.StationItemData

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
