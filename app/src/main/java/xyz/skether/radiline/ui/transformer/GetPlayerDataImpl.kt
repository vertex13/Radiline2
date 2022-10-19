package xyz.skether.radiline.ui.transformer

import xyz.skether.radiline.domain.*
import xyz.skether.radiline.ui.FavoriteStationNames
import xyz.skether.radiline.ui.GetPlayerData
import xyz.skether.radiline.ui.view.PlayerInfoData
import xyz.skether.radiline.ui.view.PlayerStatus

class GetPlayerDataImpl(
    getPlayerInfo: GetPlayerInfo,
    favoriteStationNames: FavoriteStationNames,
) : GetPlayerData {

    private val mappedValue: ObsValue<PlayerInfoData?> = combineObs(
        getPlayerInfo(), favoriteStationNames()
    ) { playerInfo, favoriteStationNames ->
        val createInfoData = { station: Station, status: PlayerStatus ->
            PlayerInfoData(
                stationName = station.name,
                currentTrack = station.currentTrack,
                playerStatus = status,
                inFavorites = favoriteStationNames.contains(station.name)
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

    override fun invoke(): ObsValue<PlayerInfoData?> = mappedValue
}
