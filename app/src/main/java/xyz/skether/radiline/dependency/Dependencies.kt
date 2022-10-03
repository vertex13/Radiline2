package xyz.skether.radiline.dependency

import xyz.skether.radiline.BuildConfig
import xyz.skether.radiline.data.AppState
import xyz.skether.radiline.data.PauseUrl
import xyz.skether.radiline.data.PlayUrl
import xyz.skether.radiline.data.StopUrl
import xyz.skether.radiline.data.backend.*
import xyz.skether.radiline.data.db.AppDatabase
import xyz.skether.radiline.data.preferences.AppSharedPreferences
import xyz.skether.radiline.data.preferences.Preferences
import xyz.skether.radiline.domain.*
import xyz.skether.radiline.system.*
import xyz.skether.radiline.ui.*
import xyz.skether.radiline.ui.transformer.*
import xyz.skether.radiline.ui.view.MainScreenDataHolder
import xyz.skether.radiline.ui.view.PlayerInfoDataHolder

class Dependencies(
    private val appContext: AppContext,
) {
    val mainScreenDataHolder: MainScreenDataHolder by lazy {
        MainScreenDataHolder(
            favoriteStationItemData = favoriteStationItemData,
            topStationItemDataWithoutFavorites = topStationItemDataWithoutFavorites,
            play = play,
            playerInfoDataHolder = playerInfoDataHolder,
        )
    }
    private val playerInfoDataHolder: PlayerInfoDataHolder by lazy {
        PlayerInfoDataHolder(
            getPlayerData = getPlayerData,
            playCurrent = playCurrent,
            pause = pause,
            addToFavorites = addToFavorites,
            removeFromFavorites = removeFromFavorites,
        )
    }
    private val appState: AppState by lazy {
        AppState(
            currentTime = currentTime,
            preferences = preferences,
            shoutcastApi = shoutcastRetrofitApi,
            playlistApi = playlistRetrofitApi,
            appDatabase = appDatabase,
            playUrl = playUrl,
            stopUrl = stopUrl,
        )
    }
    private val playingStationName: PlayingStationName by lazy {
        PlayingStationNameImpl(getPlayerInfo)
    }
    private val getPlayerData: GetPlayerData by lazy {
        GetPlayerDataImpl(getPlayerInfo, favoriteStationNames)
    }
    private val favoriteStationNames: FavoriteStationNames by lazy {
        FavoriteStationNamesImpl(favoriteStations)
    }
    private val favoriteStationItemData: FavoriteStationItemData by lazy {
        FavoriteStationItemDataImpl(favoriteStations, playingStationName)
    }
    private val topStationItemDataWithoutFavorites: TopStationItemDataWithoutFavorites by lazy {
        TopStationItemDataWithoutFavoritesImpl(
            topStations,
            favoriteStationNames,
            playingStationName
        )
    }
    private val getPlayerInfo: GetPlayerInfo
        get() = appState::playerInfo
    private val favoriteStations: FavoriteStations
        get() = appState::favoriteStations
    private val topStations: TopStations
        get() = appState::topStations
    private val play: Play
        get() = appState::play
    private val playCurrent: PlayCurrent
        get() = appState::playCurrent
    private val pause: Pause
        get() = appState::pause
    private val addToFavorites: AddToFavorites
        get() = appState::addToFavorites
    private val removeFromFavorites: RemoveFromFavorites
        get() = appState::removeFromFavorites
    private val shoutcastRetrofitApi: ShoutcastRetrofitApi by lazy {
        createShoutcastRetrofitApi(SHOUTCAST_BASE_URL, BuildConfig.SHOUTCAST_API_KEY)
    }
    private val playlistRetrofitApi: PlaylistRetrofitApi by lazy {
        createPlaylistRetrofitApi(SHOUTCAST_PLAYLIST_URL)
    }
    private val appDatabase: AppDatabase by lazy {
        AppDatabase.create(appContext)
    }
    private val preferences: Preferences by lazy {
        AppSharedPreferences(appContext)
    }
    private val currentTime: CurrentTime by lazy {
        SystemCurrentTime()
    }
    private val playUrl: PlayUrl by lazy {
        playerServicePlayUrl(appContext)
    }
    private val pauseUrl: PauseUrl by lazy {
        playerServicePauseUrl(appContext)
    }
    private val stopUrl: StopUrl by lazy {
        playerServiceStopUrl(appContext)
    }
}
