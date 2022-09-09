package xyz.skether.radiline.dependency

import xyz.skether.radiline.BuildConfig
import xyz.skether.radiline.data.AppState
import xyz.skether.radiline.data.DataManager
import xyz.skether.radiline.data.GetTopStations
import xyz.skether.radiline.data.backend.SHOUTCAST_BASE_URL
import xyz.skether.radiline.data.backend.SHOUTCAST_TOP_LIMIT
import xyz.skether.radiline.data.backend.ShoutcastRetrofit
import xyz.skether.radiline.data.backend.createShoutcastRetrofit
import xyz.skether.radiline.data.db.AppDatabase
import xyz.skether.radiline.domain.*
import xyz.skether.radiline.system.AppContext
import xyz.skether.radiline.ui.ObserveTopStationItemData
import xyz.skether.radiline.ui.data.TopStationsTransformer
import xyz.skether.radiline.ui.view.main.MainScreenDataHolder
import xyz.skether.radiline.ui.view.main.page.TopPageDataHolder

class Dependencies(
    private val appContext: AppContext,
) {
    val mainScreenDataHolder: MainScreenDataHolder by lazy {
        MainScreenDataHolder(topPageDataHolder)
    }
    private val appState: AppState by lazy {
        AppState(
            getTopStations = getTopStations,
            getFavoriteStations = { emptyList() }
        )
    }
    private val topPageDataHolder: TopPageDataHolder
        get() = TopPageDataHolder(observeTopStationItemData, play)
    private val observeTopStationItemData: ObserveTopStationItemData by lazy {
        TopStationsTransformer(observeTopStations, observeFavoriteStationIds, observeNowPlaying)
    }
    private val observeNowPlaying: ObserveNowPlaying
        get() = appState::nowPlaying
    private val observeFavoriteStations: ObserveFavoriteStations
        get() = appState::favoriteStations
    private val observeFavoriteStationIds: ObserveFavoriteStationIds
        get() = appState::favoriteStationIds
    private val observeTopStations: ObserveTopStations
        get() = appState::topStations
    private val play: Play
        get() = appState::play
    private val dataManager: DataManager by lazy {
        DataManager(
            shoutcastApiKey = BuildConfig.SHOUTCAST_API_KEY,
            shoutcastTopLimit = SHOUTCAST_TOP_LIMIT,
            shoutcastRetrofit = shoutcastRetrofit,
            appDatabase = appDatabase,
        )
    }
    private val shoutcastRetrofit: ShoutcastRetrofit by lazy {
        createShoutcastRetrofit(SHOUTCAST_BASE_URL)
    }
    private val getTopStations: GetTopStations
        get() = dataManager::getTopStations
    private val appDatabase: AppDatabase by lazy {
        AppDatabase.create(appContext)
    }
}
