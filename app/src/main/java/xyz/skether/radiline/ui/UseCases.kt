package xyz.skether.radiline.ui

import xyz.skether.radiline.domain.ObsValue
import xyz.skether.radiline.domain.StationId
import xyz.skether.radiline.ui.view.StationItemData
import xyz.skether.radiline.ui.view.PlayerData

typealias PlayingStationId = () -> ObsValue<StationId?>
typealias GetPlayerData = () -> ObsValue<PlayerData?>
typealias FavoriteStationItemData = () -> ObsValue<List<StationItemData>>
typealias TopStationItemDataWithoutFavorites = () -> ObsValue<List<StationItemData>>
