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
import xyz.skether.radiline.ui.FavoriteStationItemData
import xyz.skether.radiline.ui.GetPlayerData
import xyz.skether.radiline.ui.PlayingStationId
import xyz.skether.radiline.ui.TopStationItemDataWithoutFavorites
import xyz.skether.radiline.ui.transformer.FavoriteStationItemDataImpl
import xyz.skether.radiline.ui.transformer.GetPlayerDataImpl
import xyz.skether.radiline.ui.transformer.PlayingStationIdImpl
import xyz.skether.radiline.ui.transformer.TopStationItemDataWithoutFavoritesImpl
import xyz.skether.radiline.ui.view.MainScreenDataHolder
import xyz.skether.radiline.ui.view.PlayerDataHolder

class Dependencies(
    private val appContext: AppContext,
) {
    val mainScreenDataHolder: MainScreenDataHolder by lazy {
        MainScreenDataHolder(
            favoriteStationItemData = favoriteStationItemData,
            topStationItemDataWithoutFavorites = topStationItemDataWithoutFavorites,
            play = play,
            playerDataHolder = playerDataHolder,
        )
    }
    private val playerDataHolder: PlayerDataHolder by lazy {
        PlayerDataHolder(
            getPlayerData = getPlayerData,
            playCurrent = playCurrent,
            pause = pause,
            addToFavorites = {},
            removeFromFavorites = {},
        )
    }
    private val appState: AppState by lazy {
        AppState(
            getTopStations = getTopStations,
            getFavoriteStations = { emptyList() }
        )
    }
    private val playingStationId: PlayingStationId by lazy {
        PlayingStationIdImpl(getPlayer)
    }
    private val getPlayerData: GetPlayerData by lazy {
        GetPlayerDataImpl(getPlayer, favoriteStationIds)
    }
    private val favoriteStationItemData: FavoriteStationItemData by lazy {
        FavoriteStationItemDataImpl(favoriteStations, playingStationId)
    }
    private val topStationItemDataWithoutFavorites: TopStationItemDataWithoutFavorites by lazy {
        TopStationItemDataWithoutFavoritesImpl(
            topStations,
            favoriteStationIds,
            playingStationId
        )
    }
    private val getPlayer: GetPlayer
        get() = appState::player
    private val favoriteStations: FavoriteStations
        get() = appState::favoriteStations
    private val favoriteStationIds: FavoriteStationIds
        get() = appState::favoriteStationIds
    private val topStations: TopStations
        get() = appState::topStations
    private val play: Play
        get() = appState::play
    private val playCurrent: PlayCurrent
        get() = appState::playCurrent
    private val pause: Pause
        get() = appState::pause
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
