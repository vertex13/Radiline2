package xyz.skether.radiline.data.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import xyz.skether.radiline.domain.Station
import xyz.skether.radiline.domain.StationId

@Entity(tableName = "station")
class StationEntity(
    @PrimaryKey @ColumnInfo(name = "id") override val id: StationId,
    @ColumnInfo(name = "name") override val name: String,
    @ColumnInfo(name = "genre") override val genre: String,
    @ColumnInfo(name = "current_track") override val currentTrack: String?,
    @ColumnInfo(name = "media_type") override val mediaType: String,
    @ColumnInfo(name = "bitrate") override val bitrate: Int,
    @ColumnInfo(name = "number_of_listeners") override val numberOfListeners: Int,
) : Station
