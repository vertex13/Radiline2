package xyz.skether.radiline.domain

typealias GetPlayer = () -> ObsValue<Player>
typealias FavoriteStations = () -> ObsValue<List<Station>>
typealias TopStations = () -> ObsValue<List<Station>>
typealias Play = (StationName) -> Unit
typealias PlayCurrent = () -> Unit
typealias Pause = () -> Unit
typealias AddToFavorites = (StationName) -> Unit
typealias RemoveFromFavorites = (StationName) -> Unit
typealias CurrentTime = () -> Time
