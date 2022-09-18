package xyz.skether.radiline.ui.transformer

import xyz.skether.radiline.domain.GetPlayerInfo
import xyz.skether.radiline.domain.ObsValue
import xyz.skether.radiline.domain.PlayerInfo
import xyz.skether.radiline.domain.combineObs
import xyz.skether.radiline.ui.FavoriteStationNames
import xyz.skether.radiline.ui.GetPlayerData
import xyz.skether.radiline.ui.view.PlayerInfoData

class GetPlayerDataImpl(
    getPlayerInfo: GetPlayerInfo,
    favoriteStationNames: FavoriteStationNames,
) : GetPlayerData {

    private val mappedValue: ObsValue<PlayerInfoData?> = combineObs(
        getPlayerInfo(), favoriteStationNames()
    ) { player, favoriteStationNames ->
        if (player is PlayerInfo.Enabled) {
            PlayerInfoData(
                stationName = player.station.name,
                currentTrack = player.station.currentTrack,
                playerStatus = player.status,
                inFavorites = favoriteStationNames.contains(player.station.name)
            )
        } else {
            null
        }
    }

    override fun invoke(): ObsValue<PlayerInfoData?> = mappedValue
}
