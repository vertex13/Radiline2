package xyz.skether.radiline.data

import android.util.Log
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import xyz.skether.radiline.domain.*

typealias GetTopStations = suspend () -> List<Station>
typealias GetFavoriteStations = suspend () -> List<Station>

class AppState(
    private val getTopStations: GetTopStations,
    private val getFavoriteStations: GetFavoriteStations,
) {
    private val scope = MainScope()

    private val _player = MutableObsValue<Player>(Player.Disabled)
    val player: ObsValue<Player> get() = _player

    private val _topStations = MutableObsValue<List<Station>>(emptyList())
    val topStations: ObsValue<List<Station>> get() = _topStations

    private val _favoriteStations = MutableObsValue<List<Station>>(emptyList())
    val favoriteStations: ObsValue<List<Station>> get() = _favoriteStations

    private val _favoriteStationIds = MutableObsValue<Set<StationId>>(emptySet())
    val favoriteStationIds: ObsValue<Set<StationId>> get() = _favoriteStationIds

    init {
        scope.launch {
            try {
                _topStations.value = getTopStations()
            } catch (e: Exception) {
                Log.e("AppState", "Could not get top stations.", e)
            }
        }
    }

    fun play(stationId: StationId) {
        //
    }

    fun playCurrent() {
        //
    }

    fun pause() {
        //
    }
}
