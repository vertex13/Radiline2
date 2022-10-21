package xyz.skether.radiline.ui

import xyz.skether.radiline.domain.ObsValue
import xyz.skether.radiline.domain.StationName
import xyz.skether.radiline.ui.view.PlayerInfoData
import xyz.skether.radiline.ui.view.StationItemData

typealias PlayingStationName = ObsValue<StationName?>
typealias PlayerInfoDataValue = ObsValue<PlayerInfoData?>
typealias FavoriteStationsNames = ObsValue<Set<StationName>>
typealias FavoriteStationsItemData = ObsValue<List<StationItemData>>
typealias TopStationItemDataWithoutFavorites = ObsValue<List<StationItemData>>
