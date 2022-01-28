package local.kas.weather.repository.remote

import local.kas.weather.model.WeatherDTO
import local.kas.weather.utils.LAT
import local.kas.weather.utils.LON
import local.kas.weather.utils.YANDEX_API_KEY
import local.kas.weather.utils.YANDEX_API_URL_END_POINT
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface WeatherApi {

    @GET(YANDEX_API_URL_END_POINT)
    fun getWeather(
        @Header(YANDEX_API_KEY) apiKey: String,
        @Query(LAT) lat: Double,
        @Query(LON) lon: Double
    ): Call<WeatherDTO>
}