package com.github.vertex13.radiline.dependency

import com.github.vertex13.radiline.BuildConfig
import com.github.vertex13.radiline.data.AppState
import com.github.vertex13.radiline.data.backend.PlaylistRetrofitApi
import com.github.vertex13.radiline.data.backend.SHOUTCAST_BASE_URL
import com.github.vertex13.radiline.data.backend.SHOUTCAST_PLAYLIST_URL
import com.github.vertex13.radiline.data.backend.ShoutcastRetrofitApi
import com.github.vertex13.radiline.data.backend.createPlaylistRetrofitApi
import com.github.vertex13.radiline.data.backend.createShoutcastRetrofitApi
import com.github.vertex13.radiline.data.db.AppDatabase
import com.github.vertex13.radiline.data.preferences.AppSharedPreferences
import com.github.vertex13.radiline.data.preferences.Preferences
import com.github.vertex13.radiline.domain.AddToFavorites
import com.github.vertex13.radiline.domain.CurrentTime
import com.github.vertex13.radiline.domain.FavoriteStations
import com.github.vertex13.radiline.domain.Pause
import com.github.vertex13.radiline.domain.Play
import com.github.vertex13.radiline.domain.PlayCurrent
import com.github.vertex13.radiline.domain.PlayerInfoValue
import com.github.vertex13.radiline.domain.RemoveFromFavorites
import com.github.vertex13.radiline.domain.RunPlayer
import com.github.vertex13.radiline.domain.Stop
import com.github.vertex13.radiline.domain.TopStations
import com.github.vertex13.radiline.system.AppContext
import com.github.vertex13.radiline.system.PlayerBroadcastReceiverDataHolder
import com.github.vertex13.radiline.system.PlayerServiceDataHolder
import com.github.vertex13.radiline.system.SystemCurrentTime
import com.github.vertex13.radiline.system.UrlAudioPlayer
import com.github.vertex13.radiline.system.runPlayerService
import com.github.vertex13.radiline.ui.FavoriteStationsItemData
import com.github.vertex13.radiline.ui.FavoriteStationsNames
import com.github.vertex13.radiline.ui.PlayerInfoDataValue
import com.github.vertex13.radiline.ui.PlayingStationName
import com.github.vertex13.radiline.ui.TopStationItemDataWithoutFavorites
import com.github.vertex13.radiline.ui.transformer.transformFavoriteStationNames
import com.github.vertex13.radiline.ui.transformer.transformFavoriteStationsItemData
import com.github.vertex13.radiline.ui.transformer.transformPlayerInfoData
import com.github.vertex13.radiline.ui.transformer.transformPlayingStationName
import com.github.vertex13.radiline.ui.transformer.transformTopStationsItemDataWithoutFavorites
import com.github.vertex13.radiline.ui.view.MainScreenDataHolder
import com.github.vertex13.radiline.ui.view.PlayerInfoDataHolder

class Dependencies(
    private val appContext: AppContext,
) {
    val mainScreenDataHolder: MainScreenDataHolder
        get() = MainScreenDataHolder(
            favoriteStationsItemData = favoriteStationsItemData,
            topStationItemDataWithoutFavorites = topStationItemDataWithoutFavorites,
            play = play,
            playerInfoDataHolder = playerInfoDataHolder,
        )
    val playerServiceDataHolder: PlayerServiceDataHolder
        get() = PlayerServiceDataHolder(appContext, playerInfoValue, stop)
    val playerBroadcastReceiverDataHolder: PlayerBroadcastReceiverDataHolder
        get() = PlayerBroadcastReceiverDataHolder(playCurrent, pause, stop)
    val urlAudioPlayer: UrlAudioPlayer
        get() = UrlAudioPlayer(appContext)
    private val playerInfoDataHolder: PlayerInfoDataHolder
        get() = PlayerInfoDataHolder(
            playerInfoDataValue = playerInfoDataValue,
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
        transformPlayingStationName(playerInfoValue)
    }
    private val playerInfoDataValue: PlayerInfoDataValue by lazy {
        transformPlayerInfoData(playerInfoValue, favoriteStationsNames)
    }
    private val favoriteStationsNames: FavoriteStationsNames by lazy {
        transformFavoriteStationNames(favoriteStations)
    }
    private val favoriteStationsItemData: FavoriteStationsItemData by lazy {
        transformFavoriteStationsItemData(favoriteStations, playingStationName)
    }
    private val topStationItemDataWithoutFavorites: TopStationItemDataWithoutFavorites by lazy {
        transformTopStationsItemDataWithoutFavorites(
            topStations, favoriteStationsNames, playingStationName
        )
    }
    private val playerInfoValue: PlayerInfoValue
        get() = appState.playerInfo
    private val favoriteStations: FavoriteStations
        get() = appState.favoriteStations
    private val topStations: TopStations
        get() = appState.topStations
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
