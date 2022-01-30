package local.kas.weather.room

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [HistoryWeatherEntity::class], version = 1)
abstract class HistoryDataBase : RoomDatabase() {
    abstract fun historyWeatherDao(): HistoryWeatherDao
}