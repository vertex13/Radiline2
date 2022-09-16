package xyz.skether.radiline.ui.view

import xyz.skether.radiline.domain.MutableObsValue
import xyz.skether.radiline.domain.Player
import xyz.skether.radiline.domain.Station
import xyz.skether.radiline.domain.StationName

class PreviewStation(
    override val id: Int = 729,
    override val name: StationName = "Station name $id",
    override val genre: String = "Genre name",
    override val currentTrack: String? = "Current track",
    override val mediaType: String = "audio/mpeg",
    override val bitrate: Int = 320,
    override val numberOfListeners: Int = 8439,
) : Station

fun previewStationItemData(id: Int = 49384): StationItemData {
    return StationItemData(PreviewStation(id), false)
}

fun previewStationItemDataList(size: Int, fromId: Int = 1): List<StationItemData> {
    return List(size) { previewStationItemData(it + fromId) }
}

fun previewPlayer(): Player {
    return Player.Enabled(
        station = PreviewStation(2),
        status = Player.Status.PLAYING,
    )
}

fun previewPlayerData(): PlayerData {
    return PlayerData(
        stationName = "Station name",
        currentTrack = "Current track",
        playerStatus = Player.Status.PLAYING,
        inFavorites = true,
    )
}

fun previewMainScreenDataHolder(): MainScreenDataHolder {
    return MainScreenDataHolder(
        favoriteStationItemData = {
            MutableObsValue(previewStationItemDataList(5, fromId = 1))
        },
        topStationItemDataWithoutFavorites = {
            MutableObsValue(previewStationItemDataList(20, fromId = 10))
        },
        play = {},
        playerDataHolder = previewPlayerDataHolder(),
    )
}

fun previewPlayerDataHolder(): PlayerDataHolder {
    return PlayerDataHolder(
        getPlayerData = { MutableObsValue(previewPlayerData()) },
        playCurrent = {},
        pause = {},
        addToFavorites = {},
        removeFromFavorites = {},
    )
}
