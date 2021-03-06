package local.kas.weather.viewmodel

import local.kas.weather.model.WeatherDTO

sealed class WeatherAppState {
    data class Loading(val progress: Int) : WeatherAppState()
    data class Success(val weatherData: WeatherDTO) : WeatherAppState()
    data class Error(val error: Throwable) : WeatherAppState()
}