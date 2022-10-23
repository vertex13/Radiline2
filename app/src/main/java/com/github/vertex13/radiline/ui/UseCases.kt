package com.github.vertex13.radiline.ui

import com.github.vertex13.radiline.domain.ObsValue
import com.github.vertex13.radiline.domain.StationName
import com.github.vertex13.radiline.ui.view.PlayerInfoData
import com.github.vertex13.radiline.ui.view.StationItemData

typealias PlayingStationName = ObsValue<StationName?>
typealias PlayerInfoDataValue = ObsValue<PlayerInfoData?>
typealias FavoriteStationsNames = ObsValue<Set<StationName>>
typealias FavoriteStationsItemData = ObsValue<List<StationItemData>>
typealias TopStationItemDataWithoutFavorites = ObsValue<List<StationItemData>>
