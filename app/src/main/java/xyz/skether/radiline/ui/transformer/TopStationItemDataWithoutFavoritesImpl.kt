package xyz.skether.radiline.ui.transformer

import xyz.skether.radiline.domain.FavoriteStationIds
import xyz.skether.radiline.domain.ObsValue
import xyz.skether.radiline.domain.TopStations
import xyz.skether.radiline.domain.combineObs
import xyz.skether.radiline.ui.PlayingStationId
import xyz.skether.radiline.ui.TopStationItemDataWithoutFavorites
import xyz.skether.radiline.ui.view.StationItemData

class TopStationItemDataWithoutFavoritesImpl(
    topStations: TopStations,
    favoriteStationIds: FavoriteStationIds,
    playingStationId: PlayingStationId,
) : TopStationItemDataWithoutFavorites {

    private val combinedValue: ObsValue<List<StationItemData>> = combineObs(
        topStations(), favoriteStationIds(), playingStationId()
    ) { topStations, favoriteStationIds, playingStationId ->
        topStations.asSequence()
            .filter { !favoriteStationIds.contains(it.id) }
            .map { StationItemData(it, it.id == playingStationId) }
            .toList()
    }

    override operator fun invoke(): ObsValue<List<StationItemData>> = combinedValue
}
