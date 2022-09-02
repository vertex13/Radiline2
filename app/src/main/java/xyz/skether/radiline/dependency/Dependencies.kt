package xyz.skether.radiline.dependency

import xyz.skether.radiline.domain.*
import xyz.skether.radiline.ui.ObserveTopStationItemData
import xyz.skether.radiline.ui.data.TopStationsTransformer
import xyz.skether.radiline.ui.view.main.MainScreenDataHolder
import xyz.skether.radiline.ui.view.main.page.TopPageDataHolder

object Dependencies {
    val mainScreenDataHolder: MainScreenDataHolder by lazy {
        MainScreenDataHolder(topPageDataHolder)
    }
    private val topPageDataHolder: TopPageDataHolder by lazy {
        TopPageDataHolder(observeTopStationItemData, play)
    }
    private val observeTopStationItemData: ObserveTopStationItemData by lazy {
        TopStationsTransformer(observeTopStations, observeFavoriteStationIds, observeNowPlaying)
    }
    private val observeNowPlaying: ObserveNowPlaying by lazy {
        { MutableObsValue(null) }
    }
    private val observeFavoriteStations: ObserveFavoriteStations by lazy {
        { MutableObsValue(emptyList()) }
    }
    private val observeFavoriteStationIds: ObserveFavoriteStationIds by lazy {
        { MutableObsValue(emptySet()) }
    }
    private val observeTopStations: ObserveTopStations by lazy {
        { MutableObsValue(emptyList()) }
    }
    private val play: Play by lazy {
        {}
    }
}
