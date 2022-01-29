package local.kas.weather.room

import androidx.room.Entity
import androidx.room.PrimaryKey

const val ID = "id"
const val NAME = "name"
const val TEMPERATURE = "temperature"
//const val FEELS_LIKE = "feels_like"
//const val ICON = "icon"

@Entity(tableName = "history_weather_entity")
data class HistoryWeatherEntity(
    @PrimaryKey(autoGenerate = true) var id: Long = 0,
    var cityName: String = "cityMame",
    var temperature: Int = 0,
    var feelsLike: Int = 0,
    var icon: String = "icon"
)