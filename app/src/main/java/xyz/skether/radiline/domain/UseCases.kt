package xyz.skether.radiline.domain

typealias GetPlayer = () -> ObsValue<Player>
typealias FavoriteStations = () -> ObsValue<List<Station>>
typealias FavoriteStationIds = () -> ObsValue<Set<StationId>>
typealias TopStations = () -> ObsValue<List<Station>>
typealias Play = (StationId) -> Unit
typealias PlayCurrent = () -> Unit
typealias Pause = () -> Unit
typealias AddToFavorites = (StationId) -> Unit
typealias RemoveFromFavorites = (StationId) -> Unit
