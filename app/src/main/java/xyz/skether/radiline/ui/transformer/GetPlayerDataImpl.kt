package xyz.skether.radiline.ui.transformer

import xyz.skether.radiline.domain.GetPlayer
import xyz.skether.radiline.domain.ObsValue
import xyz.skether.radiline.domain.Player
import xyz.skether.radiline.domain.combineObs
import xyz.skether.radiline.ui.FavoriteStationNames
import xyz.skether.radiline.ui.GetPlayerData
import xyz.skether.radiline.ui.view.PlayerData

class GetPlayerDataImpl(
    getPlayer: GetPlayer,
    favoriteStationNames: FavoriteStationNames,
) : GetPlayerData {

    private val mappedValue: ObsValue<PlayerData?> = combineObs(
        getPlayer(), favoriteStationNames()
    ) { player, favoriteStationNames ->
        if (player is Player.Enabled) {
            PlayerData(
                stationName = player.station.name,
                currentTrack = player.station.currentTrack,
                playerStatus = player.status,
                inFavorites = favoriteStationNames.contains(player.station.name)
            )
        } else {
            null
        }
    }

    override fun invoke(): ObsValue<PlayerData?> = mappedValue
}
