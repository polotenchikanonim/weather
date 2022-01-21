package local.kas.weather.view.weather

import android.app.IntentService
import android.content.Intent
import android.util.Log
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import local.kas.weather.TAG
import local.kas.weather.model.WeatherDTO
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
                intent.getDoubleExtra(BUNDLE_KEY_LAT, 0.0),
                intent.getDoubleExtra(BUNDLE_KEY_LON, 0.0)
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
        val urlAddress = "https://api.weather.yandex.ru/v2/informers?lat=$lat&lon=$lon"
        try {
            val urlConnection = getUrlConnection(urlAddress)
            BufferedReader(InputStreamReader(urlConnection.inputStream)).use {
                val response: String = convertResponse(
                    BufferedReader(InputStreamReader(urlConnection.inputStream, "UTF-8"))
                )
                with(JSONObject(response).getJSONObject("fact")) {
                    LocalBroadcastManager.getInstance(applicationContext).sendBroadcast(Intent(
                        BROADCAST_ACTION
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
            addRequestProperty("X-Yandex-API-Key", "33aa2677-2f5d-44cb-9891-6b4a1781f4cd")
        }
    }

    private fun convertResponse(bufferedReader: BufferedReader): String {
        return bufferedReader.lines().collect(Collectors.joining())
    }

}