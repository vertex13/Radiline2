package xyz.skether.radiline.ui.transformer

import xyz.skether.radiline.domain.*
import xyz.skether.radiline.ui.GetPlayerData
import xyz.skether.radiline.ui.view.PlayerData

class GetPlayerDataImpl(
    getPlayer: GetPlayer,
    favoriteStationIds: FavoriteStationIds,
) : GetPlayerData {

    private val mappedValue: ObsValue<PlayerData?> = combineObs(
        getPlayer(), favoriteStationIds()
    ) { player, favoriteStationIds ->
        if (player is Player.Enabled) {
            PlayerData(
                stationId = player.station.id,
                stationName = player.station.name,
                currentTrack = player.station.currentTrack,
                playerStatus = player.status,
                inFavorites = favoriteStationIds.contains(player.station.id)
            )
        } else {
            null
        }
    }

    override fun invoke(): ObsValue<PlayerData?> = mappedValue
}
