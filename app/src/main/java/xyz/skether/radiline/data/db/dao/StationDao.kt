package xyz.skether.radiline.data.db.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import xyz.skether.radiline.data.db.entity.StationEntity
import xyz.skether.radiline.domain.Station
import xyz.skether.radiline.domain.StationName

@Dao
abstract class StationDao {

    @Query("SELECT * FROM station WHERE in_favorites = 1 ORDER BY number_of_listeners DESC")
    abstract suspend fun getFavorites(): List<StationEntity>

    @Query("SELECT * FROM station WHERE is_top = 1 ORDER BY number_of_listeners DESC")
    abstract suspend fun getTop(): List<StationEntity>

    @Query("UPDATE station SET in_favorites = :inFavorites WHERE name = :stationName")
    abstract suspend fun setFavorites(stationName: StationName, inFavorites: Boolean)

    open suspend fun update(station: Station) {
        update(
            station.name, station.id, station.genre, station.currentTrack,
            station.mediaType, station.bitrate, station.numberOfListeners
        )
    }

    @Transaction
    open suspend fun updateTop(stations: List<Station>) {
        untopAll()
        stations.forEach {
            val rowId = insertOrIgnore(it, isTop = true)
            if (rowId == -1L) {
                updateAndSetTop(
                    it.name, it.id, it.genre, it.currentTrack,
                    it.mediaType, it.bitrate, it.numberOfListeners
                )
            }
        }
        deleteUnused()
    }

    @Query("DELETE FROM station WHERE is_top = 0 AND in_favorites = 0")
    protected abstract suspend fun deleteUnused()

    @Query("UPDATE station SET is_top = 0")
    protected abstract suspend fun untopAll()

    @Query(
        """
        UPDATE station SET
          id = :id,
          genre = :genre,
          current_track = :currentTrack,
          media_type = :mediaType,
          bitrate = :bitrate,
          number_of_listeners = :numberOfListeners
        WHERE
          name = :name
        """
    )
    protected abstract suspend fun update(
        name: StationName,
        id: Long,
        genre: String,
        currentTrack: String?,
        mediaType: String,
        bitrate: Int,
        numberOfListeners: Int
    )

    @Query(
        """
        UPDATE station SET
          id = :id,
          genre = :genre,
          current_track = :currentTrack,
          media_type = :mediaType,
          bitrate = :bitrate,
          number_of_listeners = :numberOfListeners,
          is_top = 1
        WHERE
          name = :name
        """
    )
    protected abstract suspend fun updateAndSetTop(
        name: StationName,
        id: Long,
        genre: String,
        currentTrack: String?,
        mediaType: String,
        bitrate: Int,
        numberOfListeners: Int
    )

    private suspend fun insertOrIgnore(
        station: Station,
        isTop: Boolean = false,
        inFavorites: Boolean = false
    ): Long {
        return insertOrIgnore(
            station.id, station.name, station.genre, station.currentTrack,
            station.mediaType, station.bitrate, station.numberOfListeners,
            isTop, inFavorites
        )
    }

    @Query(
        """
        INSERT OR IGNORE INTO
        station(id, name, genre, current_track, media_type, bitrate, number_of_listeners, is_top, in_favorites)
        VALUES(:id, :name, :genre, :currentTrack, :mediaType, :bitrate, :numberOfListeners, :isTop, :inFavorites)
        """
    )
    protected abstract suspend fun insertOrIgnore(
        id: Long,
        name: StationName,
        genre: String,
        currentTrack: String?,
        mediaType: String,
        bitrate: Int,
        numberOfListeners: Int,
        isTop: Boolean = false,
        inFavorites: Boolean = false
    ): Long
}
