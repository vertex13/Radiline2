package xyz.skether.radiline.ui.transformer

import xyz.skether.radiline.domain.PlayerInfo
import xyz.skether.radiline.domain.PlayerInfoValue
import xyz.skether.radiline.domain.Station
import xyz.skether.radiline.domain.combineObs
import xyz.skether.radiline.ui.FavoriteStationsNames
import xyz.skether.radiline.ui.PlayerInfoDataValue
import xyz.skether.radiline.ui.view.PlayerInfoData
import xyz.skether.radiline.ui.view.PlayerStatus

fun transformPlayerInfoData(
    playerInfoValue: PlayerInfoValue,
    favoriteStationsNames: FavoriteStationsNames,
): PlayerInfoDataValue {
    return combineObs(playerInfoValue, favoriteStationsNames) { playerInfo, favNames ->
        val createInfoData = { station: Station, status: PlayerStatus ->
            PlayerInfoData(
                stationName = station.name,
                currentTrack = station.currentTrack,
                playerStatus = status,
                inFavorites = favNames.contains(station.name)
            )
        }
        when (playerInfo) {
            PlayerInfo.Disabled -> null
            is PlayerInfo.Loading -> createInfoData(playerInfo.station, PlayerStatus.LOADING)
            is PlayerInfo.Playing -> createInfoData(playerInfo.station, PlayerStatus.PLAYING)
            is PlayerInfo.Paused -> createInfoData(playerInfo.station, PlayerStatus.PAUSED)
            is PlayerInfo.Stopped -> createInfoData(playerInfo.station, PlayerStatus.PAUSED)
        }
    }
}
