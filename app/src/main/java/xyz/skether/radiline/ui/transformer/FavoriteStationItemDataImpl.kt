package xyz.skether.radiline.ui.transformer

import xyz.skether.radiline.domain.FavoriteStations
import xyz.skether.radiline.domain.ObsValue
import xyz.skether.radiline.domain.combineObs
import xyz.skether.radiline.ui.FavoriteStationItemData
import xyz.skether.radiline.ui.PlayingStationId
import xyz.skether.radiline.ui.view.StationItemData

class FavoriteStationItemDataImpl(
    favoriteStations: FavoriteStations,
    playingStationId: PlayingStationId,
) : FavoriteStationItemData {

    private val combinedValue: ObsValue<List<StationItemData>> = combineObs(
        favoriteStations(), playingStationId()
    ) { favoriteStations, playingStationId ->
        favoriteStations.map {
            StationItemData(it, it.id == playingStationId)
        }
    }

    override fun invoke(): ObsValue<List<StationItemData>> = combinedValue
}
