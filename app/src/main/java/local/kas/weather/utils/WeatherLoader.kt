//package local.kas.weather.utils
//
//
//import local.kas.weather.Method.GET
//import local.kas.weather.model.WeatherDTO
//import local.kas.weather.python.CORRECT_ANSWER
//import local.kas.weather.python.Requests
//import org.json.JSONObject
//
//class WeatherLoader(private val onWeatherLoaded: OnWeatherLoaded) {
//
//    fun loadWeather(lat: Double, lon: Double) {
//        val requests = Requests("https://api.weather.yandex.ru/v2/informers?lat=$lat&lon=$lon", GET)
//        val thread = Thread(requests)
//        thread.start()
//        thread.join()
//        val response = requests.getResponse()
//
//        if (response?.code == CORRECT_ANSWER) {
//            response.data.let {
//                val jsonObject = JSONObject(it) // fixme please. Sorry i don't know
//                val weatherDTO = convertJson(jsonObject)
//                onWeatherLoaded.onLoaded(weatherDTO)
//            }
//        } else {
//            onWeatherLoaded.onFailed()
//        }
//    }
//
//    private fun convertJson(jsonObject: JSONObject): WeatherDTO {
//        val factJ = jsonObject.getJSONObject("fact")
//        val temp = factJ.getInt("temp")
//        val feelsLike = factJ.getInt("feels_like")
//        return WeatherDTO(temp, feelsLike)
//    }
//
//    interface OnWeatherLoaded {
//        fun onLoaded(weatherDTO: WeatherDTO?)
//        fun onFailed()
//    }
//
//}