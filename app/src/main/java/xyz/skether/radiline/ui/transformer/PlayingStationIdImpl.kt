package xyz.skether.radiline.ui.transformer

import xyz.skether.radiline.domain.*
import xyz.skether.radiline.ui.PlayingStationId

class PlayingStationIdImpl(
    getPlayer: GetPlayer,
) : PlayingStationId {

    private val mappedValue: ObsValue<StationId?> = getPlayer().map {
        if (it is Player.Enabled && it.status == Player.Status.PLAYING) {
            it.station.id
        } else {
            null
        }
    }

    override fun invoke(): ObsValue<StationId?> = mappedValue
}