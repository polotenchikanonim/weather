package local.kas.weather.repository.local

import local.kas.weather.App
import local.kas.weather.model.WeatherHistory
import local.kas.weather.model.getRussianCities
import local.kas.weather.model.getWorldCities
import local.kas.weather.room.HistoryWeatherEntity


class RepositoryLocalImpl : RepositoryCities, RepositoryHistoryWeather {

    override fun getWeatherFromLocalStorageRus() = getRussianCities()

    override fun getWeatherFromLocalStorageWorld() = getWorldCities()

    override fun getAllHistoryWeather(): List<WeatherHistory> {
        return convertWeatherEntityToWeather(App.getHistoryWeatherDao().getAllHistoryWeather())
    }


    private fun convertWeatherEntityToWeather(entityList: List<HistoryWeatherEntity>): List<WeatherHistory> {
        return entityList.map { WeatherHistory(it.cityName, it.temperature, it.feelsLike, it.icon) }
    }

    override fun saveWeather(weatherHistory: WeatherHistory) {
        Thread {
            App.getHistoryWeatherDao().insert(
                convertWeatherToWeatherEntity(weatherHistory)
            )
        }.start()
    }

    private fun convertWeatherToWeatherEntity(weatherHistory: WeatherHistory) =
        HistoryWeatherEntity(
            0,
            weatherHistory.cityName,
            weatherHistory.temperature,
            weatherHistory.feelsLike,
            weatherHistory.icon
        )


}