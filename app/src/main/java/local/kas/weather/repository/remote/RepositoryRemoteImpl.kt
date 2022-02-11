package local.kas.weather.repository.remote

import local.kas.weather.BuildConfig
import local.kas.weather.utils.YANDEX_API_KEY
import local.kas.weather.utils.YANDEX_API_URL
import local.kas.weather.utils.YANDEX_API_URL_END_POINT
import okhttp3.OkHttpClient
import okhttp3.Request


class RepositoryRemoteImpl : RepositoryServerWeather {

    private var okHttpClient: OkHttpClient? = null

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