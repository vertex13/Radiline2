package xyz.skether.radiline.ui

import xyz.skether.radiline.domain.ObsValue
import xyz.skether.radiline.domain.StationName
import xyz.skether.radiline.ui.view.PlayerData
import xyz.skether.radiline.ui.view.StationItemData

typealias PlayingStationName = () -> ObsValue<StationName?>
typealias GetPlayerData = () -> ObsValue<PlayerData?>
typealias FavoriteStationNames = () -> ObsValue<Set<StationName>>
typealias FavoriteStationItemData = () -> ObsValue<List<StationItemData>>
typealias TopStationItemDataWithoutFavorites = () -> ObsValue<List<StationItemData>>
