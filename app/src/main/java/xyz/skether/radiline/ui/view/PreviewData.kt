package xyz.skether.radiline.ui.view

import xyz.skether.radiline.domain.MutableObsValue
import xyz.skether.radiline.domain.Station
import xyz.skether.radiline.domain.StationName

class PreviewStation(
    override val id: Long = 729L,
    override val name: StationName = "Station name $id",
    override val genre: String = "Genre name",
    override val currentTrack: String? = "Current track",
    override val mediaType: String = "audio/mpeg",
    override val bitrate: Int = 320,
    override val numberOfListeners: Int = 8439,
) : Station

fun previewStationItemData(id: Long = 49384L): StationItemData {
    return StationItemData(PreviewStation(id), false)
}

fun previewStationItemDataList(size: Int, fromId: Long = 1L): List<StationItemData> {
    return List(size) { previewStationItemData(it + fromId) }
}

fun previewPlayerData(): PlayerInfoData {
    return PlayerInfoData(
        stationName = "Station name",
        currentTrack = "Current track",
        playerStatus = PlayerStatus.PLAYING,
        inFavorites = true,
    )
}

fun previewMainScreenDataHolder(): MainScreenDataHolder {
    return MainScreenDataHolder(
        favoriteStationsItemData = MutableObsValue(previewStationItemDataList(5, fromId = 1L)),
        topStationItemDataWithoutFavorites = MutableObsValue(
            previewStationItemDataList(20, fromId = 10L)
        ),
        play = {},
        playerInfoDataHolder = previewPlayerDataHolder(),
    )
}

fun previewPlayerDataHolder(): PlayerInfoDataHolder {
    return PlayerInfoDataHolder(
        playerInfoDataValue = MutableObsValue(previewPlayerData()),
        playCurrent = {},
        stop = {},
        addToFavorites = {},
        removeFromFavorites = {},
    )
}
