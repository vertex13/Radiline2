package com.github.vertex13.radiline.ui.transformer

import com.github.vertex13.radiline.domain.PlayerInfo
import com.github.vertex13.radiline.domain.PlayerInfoValue
import com.github.vertex13.radiline.domain.Station
import com.github.vertex13.radiline.domain.combineObs
import com.github.vertex13.radiline.ui.FavoriteStationsNames
import com.github.vertex13.radiline.ui.PlayerInfoDataValue
import com.github.vertex13.radiline.ui.view.PlayerInfoData
import com.github.vertex13.radiline.ui.view.PlayerStatus

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
