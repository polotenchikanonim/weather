package local.kas.weather.viewmodel

import local.kas.weather.model.WeatherHistory

sealed class AppStateDB {
    data class Success(val weatherData: List<WeatherHistory>) : AppStateDB()
}