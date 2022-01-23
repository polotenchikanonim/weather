package local.kas.weather.view.weather

import android.app.IntentService
import android.content.Intent
import android.util.Log
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import local.kas.weather.BuildConfig
import local.kas.weather.TAG
import local.kas.weather.model.WeatherDTO
import local.kas.weather.utils.*
import org.json.JSONObject
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.URL
import java.util.stream.Collectors
import javax.net.ssl.HttpsURLConnection


class WeatherService(name: String = "") : IntentService(name) {

    private fun createLogMessage(message: String) {
        Log.d(TAG, message)
    }

    override fun onHandleIntent(intent: Intent?) {
        intent?.let {
            loadWeather(
                intent.getDoubleExtra(LATITUDE_EXTRA, 0.0),
                intent.getDoubleExtra(LONGITUDE_EXTRA, 0.0)
            )
        }
    }

    override fun onCreate() {
        super.onCreate()
        createLogMessage("onCreate")
    }

    override fun onDestroy() {
        super.onDestroy()
        createLogMessage("onDestroy")
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        createLogMessage("onStart $flags")
        return super.onStartCommand(intent, flags, startId)
    }

    private fun loadWeather(lat: Double, lon: Double) {
        try {
            val urlConnection =
                getUrlConnection("$YANDEX_API_URL$YANDEX_API_URL_END_POINT?lat=$lat&lon=$lon")
            BufferedReader(InputStreamReader(urlConnection.inputStream)).use {
                val response: String = convertResponse(
                    BufferedReader(InputStreamReader(urlConnection.inputStream, "UTF-8"))
                )
                with(JSONObject(response).getJSONObject("fact")) {
                    LocalBroadcastManager.getInstance(applicationContext)
                        .sendBroadcast(Intent(
                            WEATHER_INTENT_FILTER
                        ).apply {
                            putExtra(
                                BUNDLE_KEY_WEATHER, WeatherDTO(
                                    getInt("temp"), getInt("feels_like")
                                )
                            )
                        })
                }
            }
        } catch (socketTimeoutException: SocketTimeoutException) {
            println(socketTimeoutException)
        } catch (connectException: ConnectException) {
            println(connectException)
        } catch (e: IOException) {
            println(e)
        }
    }

    private fun getUrlConnection(urlAddress: String): HttpsURLConnection {
        val url = URL(urlAddress)
        return (url.openConnection() as HttpsURLConnection).apply {
            requestMethod = "GET"
            readTimeout = 2000
            addRequestProperty(YANDEX_API_KEY, BuildConfig.WEATHER_API_KEY)
        }
    }

    private fun convertResponse(bufferedReader: BufferedReader): String {
        return bufferedReader.lines().collect(Collectors.joining())
    }

}