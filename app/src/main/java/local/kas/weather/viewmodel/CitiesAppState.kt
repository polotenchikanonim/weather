package local.kas.weather.viewmodel

import local.kas.weather.model.Weather

sealed class CitiesAppState {
    data class Loading(val progress: Int) : CitiesAppState()
    data class Success(val weatherData: List<Weather>) : CitiesAppState()
    data class Error(val error: Throwable) : CitiesAppState()
}