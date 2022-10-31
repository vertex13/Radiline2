package com.github.vertex13.radiline.data

import com.github.vertex13.radiline.TOP_STATIONS_LIMIT
import com.github.vertex13.radiline.data.backend.PlaylistRetrofitApi
import com.github.vertex13.radiline.data.backend.ShoutcastRetrofitApi
import com.github.vertex13.radiline.data.db.AppDatabase
import com.github.vertex13.radiline.data.preferences.Preferences
import com.github.vertex13.radiline.domain.CurrentTime
import com.github.vertex13.radiline.domain.MutableObsValue
import com.github.vertex13.radiline.domain.ObsValue
import com.github.vertex13.radiline.domain.PlayerInfo
import com.github.vertex13.radiline.domain.RunPlayer
import com.github.vertex13.radiline.domain.Station
import com.github.vertex13.radiline.domain.StationName
import com.github.vertex13.radiline.domain.Time
import com.github.vertex13.radiline.logger.logE
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

private const val SUPPORTED_MEDIA_TYPE = "audio/mpeg"
private val CURRENT_STATION_UPDATE_INTERVAL = Time(20L * 1000L) // 20 seconds
private val TOP_UPDATE_INTERVAL = Time(6L * 60L * 60L * 1000L) // 6 hours

class AppState(
    private val currentTime: CurrentTime,
    private val preferences: Preferences,
    private val shoutcastApi: ShoutcastRetrofitApi,
    private val playlistApi: PlaylistRetrofitApi,
    private val appDatabase: AppDatabase,
    private val runPlayer: RunPlayer,
) {
    private val mainScope = MainScope()

    private val _playerInfo = MutableObsValue<PlayerInfo>(PlayerInfo.Disabled)
    val playerInfo: ObsValue<PlayerInfo> get() = _playerInfo

    private val _topStations = MutableObsValue<List<Station>>(emptyList())
    val topStations: ObsValue<List<Station>> get() = _topStations

    private val _favoriteStations = MutableObsValue<List<Station>>(emptyList())
    val favoriteStations: ObsValue<List<Station>> get() = _favoriteStations

    init {
        mainScope.launch(Dispatchers.IO) {
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

            runCurrentStationAutoUpdater()
        }
    }

    fun play(stationName: StationName) {
        mainScope.launch(Dispatchers.Default) {
            var station: Station? = findStationInCache(stationName)
            if (station == null) {
                _playerInfo.updateValue(PlayerInfo.Disabled)
                return@launch
            }

            _playerInfo.updateValue(PlayerInfo.Loading(station))
            val updatedStation = findStationAtBackend(station)
            if (updatedStation != null) {
                station = updatedStation
                updateStation(station)
            }

            try {
                val baseXspf = preferences.getTuneInBase().xspf
                    ?: error("No base xspf.")
                val playlist = withContext(Dispatchers.IO) {
                    playlistApi.getXspfPlaylist(baseXspf, station.id)
                }
                val trackUrl = playlist.trackList.firstOrNull()?.location
                    ?: error("No track location.")
                _playerInfo.updateValue(PlayerInfo.Playing(station, trackUrl))
                runPlayer()
            } catch (e: Exception) {
                logE("Could not load the track list.", e)
                _playerInfo.updateValue(PlayerInfo.Disabled)
            }
        }
    }

    fun playCurrent() {
        mainScope.launch(Dispatchers.Default) {
            val info = _playerInfo.value
            if (info is PlayerInfo.Paused) {
                _playerInfo.updateValue(PlayerInfo.Playing(info.station, info.trackUrl))
                runPlayer()
            } else if (info is PlayerInfo.Stopped) {
                _playerInfo.updateValue(PlayerInfo.Playing(info.station, info.trackUrl))
                runPlayer()
            }
        }
    }

    fun pause() {
        mainScope.launch(Dispatchers.Default) {
            val info = _playerInfo.value
            if (info is PlayerInfo.Playing) {
                _playerInfo.updateValue(
                    PlayerInfo.Paused(info.station, info.trackUrl)
                )
            }
        }
    }

    fun stop() {
        mainScope.launch(Dispatchers.Default) {
            val info = _playerInfo.value
            if (info is PlayerInfo.Playing) {
                _playerInfo.updateValue(
                    PlayerInfo.Stopped(info.station, info.trackUrl)
                )
            } else if (info is PlayerInfo.Paused) {
                _playerInfo.updateValue(
                    PlayerInfo.Stopped(info.station, info.trackUrl)
                )
            }
        }
    }

    fun addToFavorites(stationName: StationName) {
        mainScope.launch(Dispatchers.IO) {
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
        mainScope.launch(Dispatchers.IO) {
            val fav = _favoriteStations.value
            _favoriteStations.updateValue(fav.filter { it.name != stationName })
            tryOrLog("removeFromFavorites") {
                setFavoritesDB(stationName, false)
            }
        }
    }

    private fun <T> MutableObsValue<T>.updateValue(newValue: T) {
        mainScope.launch { value = newValue }
    }

    private suspend fun setFavoritesDB(stationName: StationName, inFavorites: Boolean) {
        appDatabase.stationDao().setFavorites(stationName, inFavorites)
    }

    private suspend fun updateTop() = mainScope.launch(Dispatchers.IO) {
        val topXml = try {
            shoutcastApi.getTopStations(
                limit = TOP_STATIONS_LIMIT,
                mediaType = SUPPORTED_MEDIA_TYPE
            )
        } catch (e: Exception) {
            logE("Could not get top stations.", e)
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

    private fun findStationInCache(stationName: StationName): Station? {
        return _topStations.value.find { it.name == stationName }
            ?: _favoriteStations.value.find { it.name == stationName }
    }

    private suspend fun findStationAtBackend(station: Station): Station? {
        return try {
            shoutcastApi.searchStation(
                search = station.name,
                limit = 1,
                bitrate = station.bitrate,
                mediaType = station.mediaType,
            ).stations.firstOrNull()
        } catch (e: Exception) {
            logE("Could not find the station.", e)
            null
        }
    }

    private fun updateStation(station: Station) {
        mainScope.launch(Dispatchers.IO) {
            tryOrLog("Could not update the station in the DB.") {
                appDatabase.stationDao().update(station)
            }
        }
        mainScope.launch {
            val info = playerInfo.value
            if (info is PlayerInfo.Playing && info.station.name == station.name) {
                _playerInfo.value = PlayerInfo.Playing(station, info.trackUrl)
            }

            val top = ArrayList(topStations.value)
            val indexTop = top.indexOfFirst { it.name == station.name }
            if (indexTop >= 0) {
                top[indexTop] = station
                _topStations.value = top
            }

            val fav = ArrayList(favoriteStations.value)
            val indexFav = fav.indexOfFirst { it.name == station.name }
            if (indexFav >= 0) {
                fav[indexFav] = station
                _favoriteStations.value = fav
            }
        }
    }

    private inline fun tryOrLog(message: String, fn: () -> Unit) {
        try {
            fn()
        } catch (e: Exception) {
            logE(message, e)
        }
    }

    private fun runCurrentStationAutoUpdater() {
        mainScope.launch(Dispatchers.IO) {
            while (true) {
                delay(CURRENT_STATION_UPDATE_INTERVAL.millis)
                val info = playerInfo.value
                if (info !is PlayerInfo.Playing) {
                    continue
                }
                val updatedStation = findStationAtBackend(info.station)
                if (updatedStation != null) {
                    updateStation(updatedStation)
                }
            }
        }
    }
}
