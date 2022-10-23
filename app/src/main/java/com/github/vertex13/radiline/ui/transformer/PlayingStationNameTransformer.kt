package com.github.vertex13.radiline.ui.transformer

import com.github.vertex13.radiline.domain.PlayerInfo
import com.github.vertex13.radiline.domain.PlayerInfoValue
import com.github.vertex13.radiline.domain.map
import com.github.vertex13.radiline.ui.PlayingStationName

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