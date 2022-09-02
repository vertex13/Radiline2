package xyz.skether.radiline.ui.data

import xyz.skether.radiline.domain.MutableObsValue
import xyz.skether.radiline.ui.view.main.MainScreenDataHolder
import xyz.skether.radiline.ui.view.main.page.TopPageDataHolder

fun previewStationItemData(id: Int = 49384): StationItemData = StationItemDataFromParams(
    id,
    "Station name $id",
    "Genre name",
    "Current track name",
    320,
    inFavorites = false,
    isPlaying = false,
)

fun previewStationItemDataList(size: Int): List<StationItemData> {
    return List(size) { previewStationItemData(it) }
}

fun previewMainScreenDataHolder(): MainScreenDataHolder {
    return MainScreenDataHolder(
        topPageDataHolder = previewTopPageDataHolder(),
    )
}

fun previewTopPageDataHolder(): TopPageDataHolder {
    return TopPageDataHolder({ MutableObsValue(previewStationItemDataList(20)) }, {})
}
