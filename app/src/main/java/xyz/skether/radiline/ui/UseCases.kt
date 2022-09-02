package xyz.skether.radiline.ui

import xyz.skether.radiline.domain.ObsValue
import xyz.skether.radiline.ui.data.StationItemData

typealias ObserveTopStationItemData = () -> ObsValue<List<StationItemData>>
