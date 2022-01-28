package local.kas.weather.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "history_weather_entity")
data class HistoryWeatherEntity(
    @PrimaryKey(autoGenerate = true) val id: Long,
    val cityName: String,
    val temperature: Int,
    val feelsLike: Int,
    val icon: String
)