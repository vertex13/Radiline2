package xyz.skether.radiline.ui.transformer

import xyz.skether.radiline.domain.ObsValue
import xyz.skether.radiline.domain.TopStations
import xyz.skether.radiline.domain.combineObs
import xyz.skether.radiline.ui.FavoriteStationNames
import xyz.skether.radiline.ui.PlayingStationName
import xyz.skether.radiline.ui.TopStationItemDataWithoutFavorites
import xyz.skether.radiline.ui.view.StationItemData

class TopStationItemDataWithoutFavoritesImpl(
    topStations: TopStations,
    favoriteStationNames: FavoriteStationNames,
    playingStationName: PlayingStationName,
) : TopStationItemDataWithoutFavorites {

    private val combinedValue: ObsValue<List<StationItemData>> = combineObs(
        topStations(), favoriteStationNames(), playingStationName()
    ) { topStations, favoriteStationNames, playingStationName ->
        topStations.asSequence()
            .filter { !favoriteStationNames.contains(it.name) }
            .map { StationItemData(it, it.name == playingStationName) }
            .toList()
    }

    override operator fun invoke(): ObsValue<List<StationItemData>> = combinedValue
}
