package xyz.skether.radiline.data

import xyz.skether.radiline.data.backend.ShoutcastRetrofit
import xyz.skether.radiline.domain.Station

class DataManager(
    private val shoutcastApiKey: String,
    private val shoutcastTopLimit: Int,
    private val shoutcastRetrofit: ShoutcastRetrofit,
) {
    suspend fun getTopStations(): List<Station> {
        val result = shoutcastRetrofit.getTopStations(
            key = shoutcastApiKey,
            limit = shoutcastTopLimit
        )
        return result.stations
    }
}