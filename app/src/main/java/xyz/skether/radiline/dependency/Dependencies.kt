package xyz.skether.radiline.dependency

import xyz.skether.radiline.BuildConfig
import xyz.skether.radiline.data.AppState
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
    val mainScreenDataHolder: MainScreenDataHolder
        get() = MainScreenDataHolder(
            favoriteStationItemData = favoriteStationItemData,
            topStationItemDataWithoutFavorites = topStationItemDataWithoutFavorites,
            play = play,
            playerInfoDataHolder = playerInfoDataHolder,
        )
    val playerServiceDataHolder: PlayerServiceDataHolder
        get() = PlayerServiceDataHolder(appContext, getPlayerInfo(), stop)
    val playerBroadcastReceiverDataHolder: PlayerBroadcastReceiverDataHolder
        get() = PlayerBroadcastReceiverDataHolder(playCurrent, pause, stop)
    val urlAudioPlayer: UrlAudioPlayer
        get() = UrlAudioPlayer(appContext)
    private val playerInfoDataHolder: PlayerInfoDataHolder
        get() = PlayerInfoDataHolder(
            getPlayerData = getPlayerData,
            playCurrent = playCurrent,
            stop = stop,
            addToFavorites = addToFavorites,
            removeFromFavorites = removeFromFavorites,
        )
    private val appState: AppState by lazy {
        AppState(
            currentTime = currentTime,
            preferences = preferences,
            shoutcastApi = shoutcastRetrofitApi,
            playlistApi = playlistRetrofitApi,
            appDatabase = appDatabase,
            runPlayer = runPlayer,
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
    private val stop: Stop
        get() = appState::stop
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
    private val runPlayer: RunPlayer by lazy {
        runPlayerService(appContext)
    }
}
