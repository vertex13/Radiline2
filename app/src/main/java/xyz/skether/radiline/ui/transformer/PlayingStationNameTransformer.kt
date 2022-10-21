package xyz.skether.radiline.ui.transformer

import xyz.skether.radiline.domain.PlayerInfo
import xyz.skether.radiline.domain.PlayerInfoValue
import xyz.skether.radiline.domain.map
import xyz.skether.radiline.ui.PlayingStationName

fun transformPlayingStationName(
    playerInfoValue: PlayerInfoValue,
): PlayingStationName {
    return playerInfoValue.map {
        if (it is PlayerInfo.Playing) {
            it.station.name
        } else {
            null
        }
    }
}