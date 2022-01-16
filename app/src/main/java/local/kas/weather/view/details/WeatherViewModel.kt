package local.kas.weather.view.details

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import local.kas.weather.Method
import local.kas.weather.Response
import local.kas.weather.model.City
import local.kas.weather.model.WeatherDTO
import local.kas.weather.python.CORRECT_ANSWER
import local.kas.weather.python.Requests
import local.kas.weather.viewmodel.WeatherAppState
import org.json.JSONObject

class WeatherViewModel(private val liveDataToObserve: MutableLiveData<WeatherAppState> = MutableLiveData()) :
    ViewModel() {

    fun getLiveData() = liveDataToObserve

    fun loadWeather(city: City) {
        liveDataToObserve.value = WeatherAppState.Loading(0)

        val response = requestWeather(city.latitude, city.longitude)
        response?.let {
            if (it.code == CORRECT_ANSWER) {
                val weatherDTO = convertJson(JSONObject(response.data)) // fixme please. Sorry i don't know
                liveDataToObserve.postValue(
                    WeatherAppState.Success(
                        city, weatherDTO
                    )
                )
            }
        }
    }

    private fun requestWeather(lat: Double, lon: Double): Response? {
        val requests =
            Requests("https://api.weather.yandex.ru/v2/informers?lat=$lat&lon=$lon", Method.GET)
        val thread = Thread(requests)
        thread.start()
        thread.join()
        return requests.getResponse()
    }

    private fun convertJson(jsonObject: JSONObject): WeatherDTO {
        val factJ = jsonObject.getJSONObject("fact")
        val temp = factJ.getInt("temp")
        val feelsLike = factJ.getInt("feels_like")
        return WeatherDTO(temp, feelsLike)
    }

}