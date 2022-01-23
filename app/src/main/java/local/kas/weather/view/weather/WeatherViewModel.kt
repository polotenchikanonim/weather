package local.kas.weather.view.weather

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import local.kas.weather.model.WeatherDTO
import local.kas.weather.repository.RepositoryImpl
import local.kas.weather.viewmodel.WeatherAppState
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException

class WeatherViewModel(
    private val liveData: MutableLiveData<WeatherAppState> = MutableLiveData()
) : ViewModel() {

    private val repositoryImpl: RepositoryImpl by lazy {
        RepositoryImpl()
    }

    fun getLiveData() = liveData

    fun getWeatherFromServer(lat: Double, lon: Double) {
        liveData.postValue(WeatherAppState.Loading(0))

//        repositoryImpl.getWeatherFromServerRetrofit(
//            lat, lon, callback
//        )
        repositoryImpl.getWeatherFromServerHttp(
            lat, lon, callbackHttp
        )
    }

    private val callback = object : Callback<WeatherDTO> {

        override fun onResponse(call: Call<WeatherDTO>, response: Response<WeatherDTO>) {
            if (response.isSuccessful) {
                response.body()?.let {
                    liveData.postValue(WeatherAppState.Success(it))
                }
            }
        }

        override fun onFailure(call: Call<WeatherDTO>, t: Throwable) {
            TODO("Not yet implemented")
        }

    }

    private val callbackHttp = object : okhttp3.Callback{
        override fun onFailure(call: okhttp3.Call, e: IOException) {
            TODO("Not yet implemented")
        }

        override fun onResponse(call: okhttp3.Call, response: okhttp3.Response) {
            if (response.isSuccessful) {
                response.body()?.let {
                    with(JSONObject(it.string()).getJSONObject("fact")) {
                        liveData.postValue(WeatherAppState.Success(WeatherDTO(getInt("temp"),getInt("feels_like"))))
                    }
                }
            }
        }
    }
}