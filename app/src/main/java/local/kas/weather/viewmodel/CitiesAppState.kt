package local.kas.weather.viewmodel

import local.kas.weather.model.City

sealed class CitiesAppState {
    data class Success(val weatherData: List<City>) : CitiesAppState()
}