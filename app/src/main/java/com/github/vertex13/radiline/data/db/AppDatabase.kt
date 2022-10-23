package com.github.vertex13.radiline.data.db

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.github.vertex13.radiline.data.db.dao.StationDao
import com.github.vertex13.radiline.data.db.entity.StationEntity
import com.github.vertex13.radiline.system.AppContext

private const val DB_NAME = "app.db"

@Database(
    version = 1,
    entities = [StationEntity::class]
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun stationDao(): StationDao

    companion object {
        fun create(appContext: AppContext): AppDatabase {
            return Room.databaseBuilder(
                appContext.value,
                AppDatabase::class.java,
                DB_NAME
            ).build()
        }
    }
}
