package local.kas.weather.python

import local.kas.weather.Method
import local.kas.weather.Response
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader

import java.net.URL
import java.util.stream.Collectors
import javax.net.ssl.HttpsURLConnection


const val CORRECT_ANSWER = 200

class Requests(urlAddress: String, private val method: Method) : Runnable {

    private var response: Response? = null
    private val url: URL = URL(urlAddress)


    override fun run() {
        if (method === Method.POST) {
            post(url)
        } else if (method === Method.GET) {
            get()
        }
    }

    private fun get() {
        response = request()
    }

    private fun request(): Response {
        return try {
            val urlConnection = (url.openConnection() as HttpsURLConnection).apply {
                requestMethod = method.name
                readTimeout = 2000
                addRequestProperty("X-Yandex-API-Key", "33aa2677-2f5d-44cb-9891-6b4a1781f4cd")
            }
            val response = convertResponse(
                BufferedReader(InputStreamReader(urlConnection.inputStream, "UTF-8"))
            )
            urlConnection.disconnect()
            Response(urlConnection.responseCode, response)

        } catch (e: IOException) {
            Response(0, e.toString())
        }
    }

    private fun convertResponse(bufferedReader: BufferedReader): String? {
        return bufferedReader.lines().collect(Collectors.joining())
    }


    private fun post(url: URL) {
        println(url)
    }

    fun getResponse(): Response? {
        return response
    }
}
