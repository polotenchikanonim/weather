package local.kas.weather.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import local.kas.weather.model.WeatherDTO
import local.kas.weather.model.WeatherHistory
import local.kas.weather.repository.local.RepositoryLocalImpl
import local.kas.weather.repository.remote.RepositoryRemoteImpl
import org.json.JSONObject
import java.io.IOException

class WeatherViewModel(
    private val liveData: MutableLiveData<WeatherAppState> = MutableLiveData(),
    private val repositoryLocalImpl: RepositoryLocalImpl = RepositoryLocalImpl()
) : ViewModel() {

    private val repositoryImpl: RepositoryRemoteImpl by lazy {
        RepositoryRemoteImpl()
    }

    fun getLiveData() = liveData

    fun getWeatherFromServer(lat: Double, lon: Double) {
        liveData.postValue(WeatherAppState.Loading(0))
        repositoryImpl.getWeatherFromServerHttp(
            lat, lon, callbackHttp
        )
    }

    private val callbackHttp = object : okhttp3.Callback {
        override fun onFailure(call: okhttp3.Call, e: IOException) {
            TODO("Not yet implemented")
        }

        override fun onResponse(call: okhttp3.Call, response: okhttp3.Response) {
            if (response.isSuccessful) {

                response.body()?.let {
                    with(JSONObject(it.string()).getJSONObject("fact")) {
                        val temp = getInt("temp")
                        val feelsLike = getInt("feels_like")
                        val icon = getString("icon")
                        liveData.postValue(
                            WeatherAppState.Success(
                                WeatherDTO(
                                    temp,
                                    feelsLike,
                                    icon
                                )
                            )
                        )
                    }
                }
            }
        }
    }

    fun saveWeather(weatherHistory: WeatherHistory) {
        repositoryLocalImpl.saveWeather(weatherHistory)
    }
}
//    private val callbackRetrofit = object : Callback<WeatherDTO> {
//        override fun onResponse(call: Call<WeatherDTO>, response: Response<WeatherDTO>) {
//            if (response.isSuccessful) {
//                response.body()?.let {
//                    liveData.postValue(WeatherAppState.Success(it))
//                }
//            }
//        }
//        override fun onFailure(call: Call<WeatherDTO>, t: Throwable) {
//            TODO("Not yet implemented")
//        }
//    }

//        repositoryImpl.getWeatherFromServerRetrofit(
//            lat, lon, callback
//        )