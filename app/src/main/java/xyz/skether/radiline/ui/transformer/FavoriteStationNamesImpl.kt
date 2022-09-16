package xyz.skether.radiline.ui.transformer

import xyz.skether.radiline.domain.FavoriteStations
import xyz.skether.radiline.domain.ObsValue
import xyz.skether.radiline.domain.StationName
import xyz.skether.radiline.domain.map
import xyz.skether.radiline.ui.FavoriteStationNames

class FavoriteStationNamesImpl(
    favoriteStations: FavoriteStations
) : FavoriteStationNames {

    private val mappedValue: ObsValue<Set<StationName>> = favoriteStations().map { stations ->
        stations.asSequence().map { it.name }.toHashSet()
    }

    override fun invoke(): ObsValue<Set<StationName>> = mappedValue
}
