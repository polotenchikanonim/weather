package local.kas.weather.repository.local

import local.kas.weather.model.WeatherHistory


interface RepositoryHistoryWeather {
    fun getAllHistoryWeather(): List<WeatherHistory>
    fun saveWeather(weatherHistory: WeatherHistory)
}