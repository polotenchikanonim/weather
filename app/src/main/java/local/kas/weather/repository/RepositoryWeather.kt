package local.kas.weather.repository

import local.kas.weather.model.WeatherDTO
import retrofit2.Callback

interface RepositoryWeather {
    fun getWeatherFromServerRetrofit(lat: Double, lon: Double, callback: Callback<WeatherDTO>)
    fun getWeatherFromServerHttp(lat: Double, lon: Double, callbackHttp: okhttp3.Callback)

}