package xyz.skether.radiline.domain

typealias ObserveNowPlaying = () -> ObsValue<Station?>
typealias ObserveFavoriteStations = () -> ObsValue<List<Station>>
typealias ObserveFavoriteStationIds = () -> ObsValue<Set<StationId>>
typealias ObserveTopStations = () -> ObsValue<List<Station>>
typealias Play = (StationId) -> Unit
