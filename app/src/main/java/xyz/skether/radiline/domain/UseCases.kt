package xyz.skether.radiline.domain

typealias GetPlayerInfo = () -> ObsValue<PlayerInfo>
typealias FavoriteStations = () -> ObsValue<List<Station>>
typealias TopStations = () -> ObsValue<List<Station>>
typealias Play = (StationName) -> Unit
typealias PlayCurrent = () -> Unit
typealias Pause = () -> Unit
typealias Stop = () -> Unit
typealias AddToFavorites = (StationName) -> Unit
typealias RemoveFromFavorites = (StationName) -> Unit
typealias CurrentTime = () -> Time
typealias RunPlayer = () -> Unit
