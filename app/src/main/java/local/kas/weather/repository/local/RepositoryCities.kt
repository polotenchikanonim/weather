package local.kas.weather.repository.local

import local.kas.weather.model.City

interface RepositoryCities {
    fun getWeatherFromLocalStorageRus(): List<City>
    fun getWeatherFromLocalStorageWorld(): List<City>
}