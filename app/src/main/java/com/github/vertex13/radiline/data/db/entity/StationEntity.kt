package com.github.vertex13.radiline.data.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.github.vertex13.radiline.domain.Station
import com.github.vertex13.radiline.domain.StationName

@Entity(tableName = "station")
class StationEntity(
    @PrimaryKey @ColumnInfo(name = "name") override val name: StationName,
    @ColumnInfo(name = "id") override val id: Long,
    @ColumnInfo(name = "genre") override val genre: String,
    @ColumnInfo(name = "current_track") override val currentTrack: String?,
    @ColumnInfo(name = "media_type") override val mediaType: String,
    @ColumnInfo(name = "bitrate") override val bitrate: Int,
    @ColumnInfo(name = "number_of_listeners") override val numberOfListeners: Int,
    @ColumnInfo(name = "is_top", defaultValue = "0") val isTop: Boolean,
    @ColumnInfo(name = "in_favorites", defaultValue = "0") val inFavorites: Boolean,
) : Station
