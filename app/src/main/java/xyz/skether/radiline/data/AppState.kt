package xyz.skether.radiline.data

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import xyz.skether.radiline.data.backend.PlaylistRetrofitApi
import xyz.skether.radiline.data.backend.ShoutcastRetrofitApi
import xyz.skether.radiline.data.db.AppDatabase
import xyz.skether.radiline.data.preferences.Preferences
import xyz.skether.radiline.domain.*

private const val TOP_LIMIT = 50
private val TOP_UPDATE_INTERVAL = Time(6L * 60 * 60 * 1000) // 6 hours

class AppState(
    private val currentTime: CurrentTime,
    private val preferences: Preferences,
    private val shoutcastApi: ShoutcastRetrofitApi,
    private val playlistApi: PlaylistRetrofitApi,
    private val appDatabase: AppDatabase,
    private val playUrl: PlayUrl,
    private val stopUrl: StopUrl,
) {
    private val scope = MainScope()

    private val _playerInfo = MutableObsValue<PlayerInfo>(PlayerInfo.Disabled)
    val playerInfo: ObsValue<PlayerInfo> get() = _playerInfo

    private val _topStations = MutableObsValue<List<Station>>(emptyList())
    val topStations: ObsValue<List<Station>> get() = _topStations

    private val _favoriteStations = MutableObsValue<List<Station>>(emptyList())
    val favoriteStations: ObsValue<List<Station>> get() = _favoriteStations

    init {
        scope.launch(Dispatchers.IO) {
            var fav = emptyList<Station>()
            tryOrLog("getFavoritesFromDB") {
                fav = appDatabase.stationDao().getFavorites()
            }
            _favoriteStations.updateValue(fav)

            var top = emptyList<Station>()
            tryOrLog("getTopFromDB") {
                top = appDatabase.stationDao().getTop()
            }
            _topStations.updateValue(top)
            val topUpdateTime = preferences.getLastTopUpdate() + TOP_UPDATE_INTERVAL
            if (_topStations.value.isEmpty()
                || currentTime().after(topUpdateTime)
            ) {
                updateTop()
            }
        }
    }

    fun play(stationName: StationName) {
        scope.launch(Dispatchers.Default) {
            val station = findStation(stationName)
            if (station == null) {
                _playerInfo.updateValue(PlayerInfo.Disabled)
            } else {
                play(station)
            }
        }
    }

    fun playCurrent() {
        scope.launch(Dispatchers.Default) {
            val playerInfo = _playerInfo.value
            if (playerInfo is PlayerInfo.Enabled) {
                play(playerInfo.station)
            }
        }
    }

    private suspend fun play(station: Station) {
        _playerInfo.updateValue(PlayerInfo.Enabled(station, PlayerInfo.Status.LOADING))
        try {
            val baseXspf = preferences.getTuneInBase().xspf ?: error("No base xspf.")
            val playlist = withContext(Dispatchers.IO) {
                playlistApi.getXspfPlaylist(baseXspf, station.id)
            }
            val url = playlist.trackList.firstOrNull()?.location ?: error("No track location.")
            playUrl(url)
        } catch (e: Exception) {
            // todo add error
            Log.e("AppState", "Player exception.", e)
            _playerInfo.updateValue(PlayerInfo.Disabled)
            return
        }
        _playerInfo.updateValue(PlayerInfo.Enabled(station, PlayerInfo.Status.PLAYING))
    }

    fun pause() {
        scope.launch(Dispatchers.Default) {
            val playerInfo = _playerInfo.value
            if (playerInfo is PlayerInfo.Enabled) {
                _playerInfo.updateValue(
                    PlayerInfo.Enabled(playerInfo.station, PlayerInfo.Status.PAUSED)
                )
                stopUrl()
            }
        }
    }


    fun addToFavorites(stationName: StationName) {
        scope.launch(Dispatchers.IO) {
            val fav = _favoriteStations.value
            val inFav = fav.find { it.name == stationName }
            if (inFav != null) {
                return@launch
            }
            val inTop = _topStations.value.find { it.name == stationName } ?: return@launch
            _favoriteStations.updateValue(fav + inTop)
            tryOrLog("addToFavorites") {
                setFavoritesDB(stationName, true)
            }
        }
    }

    fun removeFromFavorites(stationName: StationName) {
        scope.launch(Dispatchers.IO) {
            val fav = _favoriteStations.value
            _favoriteStations.updateValue(fav.filter { it.name != stationName })
            tryOrLog("removeFromFavorites") {
                setFavoritesDB(stationName, false)
            }
        }
    }

    private fun <T> MutableObsValue<T>.updateValue(newValue: T) {
        scope.launch { value = newValue }
    }

    private suspend fun setFavoritesDB(stationName: StationName, inFavorites: Boolean) {
        appDatabase.stationDao().setFavorites(stationName, inFavorites)
    }

    private suspend fun updateTop() = scope.launch(Dispatchers.IO) {
        val topXml = try {
            shoutcastApi.getTopStations(limit = TOP_LIMIT, mediaType = "audio/mpeg")
        } catch (e: Exception) {
            Log.e("AppState", "updateTop", e)
            if (_topStations.value.isEmpty()) {
                // todo nothing to show error
            }
            return@launch
        }
        topXml.tuneIn?.let {
            preferences.setTuneInBase(it)
        }
        val stations = topXml.stations.distinctBy { it.name }
        _topStations.updateValue(stations)
        appDatabase.stationDao().updateTop(stations)
        preferences.setLastTopUpdate(currentTime())
    }

    private fun findStation(stationName: StationName): Station? {
        return _topStations.value.find { it.name == stationName }
            ?: _favoriteStations.value.find { it.name == stationName }
    }

    private inline fun tryOrLog(message: String, fn: () -> Unit) {
        try {
            fn()
        } catch (e: Exception) {
            Log.e("AppState", message, e)
        }
    }
}
