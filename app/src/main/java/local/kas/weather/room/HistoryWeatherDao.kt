package local.kas.weather.room

import android.database.Cursor
import androidx.room.*

@Dao
interface HistoryWeatherDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(entity: HistoryWeatherEntity)

    @Delete
    fun delete(entity: HistoryWeatherEntity)

    @Update
    fun update(entity: HistoryWeatherEntity)

    @Query("SELECT * FROM history_weather_entity")
    fun getAllHistoryWeather(): List<HistoryWeatherEntity>

    // lesson 9
    @Query("SELECT * FROM history_weather_entity WHERE id=:id")
    fun getHistoryLine(id: Long): Cursor
}