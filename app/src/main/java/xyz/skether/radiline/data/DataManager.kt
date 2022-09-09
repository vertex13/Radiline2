package xyz.skether.radiline.data

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import xyz.skether.radiline.data.backend.ShoutcastRetrofit
import xyz.skether.radiline.data.db.AppDatabase
import xyz.skether.radiline.domain.Station

class DataManager(
    private val shoutcastApiKey: String,
    private val shoutcastTopLimit: Int,
    private val shoutcastRetrofit: ShoutcastRetrofit,
    private val appDatabase: AppDatabase,
) {
    suspend fun getTopStations(): List<Station> {
        return withContext(Dispatchers.IO) {
            val stationDao = appDatabase.stationDao()
            val resultFromDB = try {
                stationDao.getTop()
            } catch (e: Exception) {
                emptyList()
            }
            if (resultFromDB.isNotEmpty()) {
                return@withContext resultFromDB
            }

            val resultFromBackend = shoutcastRetrofit.getTopStations(
                key = shoutcastApiKey,
                limit = shoutcastTopLimit
            )
            try {
                stationDao.updateAll(resultFromBackend.stations)
            } catch (e: Exception) {
            }
            return@withContext resultFromBackend.stations
        }
    }
}
