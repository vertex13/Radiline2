package xyz.skether.radiline.data.db.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import xyz.skether.radiline.data.db.entity.StationEntity
import xyz.skether.radiline.domain.Station
import xyz.skether.radiline.domain.StationId

@Dao
abstract class StationDao {
    @Query("SELECT * FROM station ORDER BY number_of_listeners DESC")
    abstract suspend fun getTop(): List<StationEntity>

    @Query(
        """
        INSERT OR REPLACE INTO
        station(id, name, genre, current_track, media_type, bitrate, number_of_listeners)
        VALUES(:id, :name, :genre, :currentTrack, :mediaType, :bitrate, :numberOfListeners)
        """
    )
    abstract suspend fun insert(
        id: StationId,
        name: String,
        genre: String,
        currentTrack: String?,
        mediaType: String,
        bitrate: Int,
        numberOfListeners: Int
    )

    @Query("DELETE FROM station")
    abstract suspend fun deleteAll()

    @Transaction
    open suspend fun updateAll(stations: List<Station>) {
        deleteAll()
        stations.forEach {
            insert(
                it.id, it.name, it.genre, it.currentTrack,
                it.mediaType, it.bitrate, it.numberOfListeners
            )
        }
    }
}
