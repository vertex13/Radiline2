package xyz.skether.radiline.ui.transformer

import xyz.skether.radiline.domain.*
import xyz.skether.radiline.ui.PlayingStationName

class PlayingStationNameImpl(
    getPlayerInfo: GetPlayerInfo,
) : PlayingStationName {

    private val mappedValue: ObsValue<StationName?> = getPlayerInfo().map {
        if (it is PlayerInfo.Playing) {
            it.station.name
        } else {
            null
        }
    }

    override fun invoke(): ObsValue<StationName?> = mappedValue
}