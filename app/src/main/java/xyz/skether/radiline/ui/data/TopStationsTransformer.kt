package xyz.skether.radiline.ui.data

import xyz.skether.radiline.domain.*
import xyz.skether.radiline.ui.ObserveTopStationItemData

class TopStationsTransformer(
    observeTopStations: ObserveTopStations,
    observeFavoriteStationIds: ObserveFavoriteStationIds,
    observeNowPlaying: ObserveNowPlaying,
) : ObserveTopStationItemData {

    private val combinedObsValue: ObsValue<List<StationItemData>> = combineObs(
        observeTopStations(), observeFavoriteStationIds(), observeNowPlaying()
    ) { topStations, favoriteStationIds, nowPlaying ->
        topStations.map { station ->
            StationItemDataFromDomain(
                station = station,
                inFavorites = favoriteStationIds.contains(station.id),
                isPlaying = nowPlaying != null && station.id == nowPlaying.id
            )
        }
    }

    override operator fun invoke(): ObsValue<List<StationItemData>> = combinedObsValue
}
