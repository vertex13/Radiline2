package xyz.skether.radiline.ui.transformer

import xyz.skether.radiline.domain.*
import xyz.skether.radiline.ui.PlayingStationName

class PlayingStationNameImpl(
    getPlayer: GetPlayer,
) : PlayingStationName {

    private val mappedValue: ObsValue<StationName?> = getPlayer().map {
        if (it is Player.Enabled && it.status == Player.Status.PLAYING) {
            it.station.name
        } else {
            null
        }
    }

    override fun invoke(): ObsValue<StationName?> = mappedValue
}