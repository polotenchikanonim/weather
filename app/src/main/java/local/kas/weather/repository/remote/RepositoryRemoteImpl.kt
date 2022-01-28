package local.kas.weather.repository.remote

import com.google.gson.GsonBuilder
import local.kas.weather.BuildConfig
import local.kas.weather.model.WeatherDTO
import local.kas.weather.utils.YANDEX_API_KEY
import local.kas.weather.utils.YANDEX_API_URL
import local.kas.weather.utils.YANDEX_API_URL_END_POINT
import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Callback
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class RepositoryRemoteImpl : RepositoryServerWeather {

    private var okHttpClient: OkHttpClient? = null
//    override fun getWeatherFromServerRetrofit(lat: Double, lon: Double, callback: Callback<WeatherDTO>) {
//        val builder = Retrofit.Builder()
//            .baseUrl(YANDEX_API_URL)
//            .addConverterFactory(
//                GsonConverterFactory.create(
//                    GsonBuilder().setLenient().create()
//                )
//            )
//            .build().create(WeatherApi::class.java)
//        builder.getWeather(BuildConfig.WEATHER_API_KEY, lat, lon).enqueue(callback)
//    }


    override fun getWeatherFromServerHttp(
        lat: Double,
        lon: Double,
        callbackHttp: okhttp3.Callback
    ) {
        if (okHttpClient == null) {
            okHttpClient = OkHttpClient()
        }
        val builder = Request.Builder().apply {
            header(YANDEX_API_KEY, BuildConfig.WEATHER_API_KEY)
            url("$YANDEX_API_URL$YANDEX_API_URL_END_POINT?lat=$lat&lon=$lon")
        }
        okHttpClient?.newCall(builder.build())?.enqueue(callbackHttp)
    }

}