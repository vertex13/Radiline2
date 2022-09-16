package xyz.skether.radiline.ui.transformer

import xyz.skether.radiline.domain.FavoriteStations
import xyz.skether.radiline.domain.ObsValue
import xyz.skether.radiline.domain.combineObs
import xyz.skether.radiline.ui.FavoriteStationItemData
import xyz.skether.radiline.ui.PlayingStationName
import xyz.skether.radiline.ui.view.StationItemData

class FavoriteStationItemDataImpl(
    favoriteStations: FavoriteStations,
    playingStationName: PlayingStationName,
) : FavoriteStationItemData {

    private val combinedValue: ObsValue<List<StationItemData>> = combineObs(
        favoriteStations(), playingStationName()
    ) { favoriteStations, playingStationName ->
        favoriteStations.map {
            StationItemData(it, it.name == playingStationName)
        }
    }

    override fun invoke(): ObsValue<List<StationItemData>> = combinedValue
}
