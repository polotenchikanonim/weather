//package local.kas.weather.python
//
//import local.kas.weather.Method
//import local.kas.weather.Response
//import java.io.BufferedReader
//import java.net.URL
//import java.util.stream.Collectors
//import javax.net.ssl.HttpsURLConnection
//
//
//const val CORRECT_ANSWER = 200
//const val INCORRECT_ANSWER = 0
//
//class Requests(urlAddress: String, private val method: Method) : Runnable {
//
//    private var response: Response? = null
//    private val url: URL = URL(urlAddress)
//
//
//    override fun run() {
//        if (method === Method.POST) {
//            post(url)
//        } else if (method === Method.GET) {
//            get()
//        }
//    }
//
//    private fun get() {
//        request()
//    }
//
//    private fun request() {
//        Thread {
//
////            var urlConnection: HttpsURLConnection? = null
////            return try {
////                urlConnection = getUrlConnection()
////                BufferedReader(InputStreamReader(urlConnection.inputStream)).use {
////                    val response = convertResponse(
////                        BufferedReader(InputStreamReader(urlConnection.inputStream, "UTF-8"))
////                    )
////                    Response(urlConnection.responseCode, response)
////                }
////            } catch (socketTimeoutException: SocketTimeoutException) {
////                Response(INCORRECT_ANSWER, socketTimeoutException.toString())
////            } catch (connectException: ConnectException) {
////                Response(INCORRECT_ANSWER, connectException.toString())
////            } catch (e: IOException) {
////                Response(INCORRECT_ANSWER, e.toString())
////            } finally {
////                assert(urlConnection != null)
////                urlConnection!!.disconnect()
////            }
//        }.start()
//
//    }
//
//    private fun getUrlConnection(): HttpsURLConnection {
//        return (url.openConnection() as HttpsURLConnection).apply {
//            requestMethod = method.name
//            readTimeout = 2000
//            addRequestProperty("X-Yandex-API-Key", "33aa2677-2f5d-44cb-9891-6b4a1781f4cd")
//        }
//    }
//
//    private fun convertResponse(bufferedReader: BufferedReader): String? {
//        return bufferedReader.lines().collect(Collectors.joining())
//    }
//
//
//    private fun post(url: URL) {
//        println(url)
//    }
//
//    fun getResponse(): Response? {
//        return response
//    }
//}
