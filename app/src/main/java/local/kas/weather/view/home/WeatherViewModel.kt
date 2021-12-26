package local.kas.weather.view.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import local.kas.weather.model.Weather
import local.kas.weather.viewmodel.AppState

class WeatherViewModel(
    private val liveData: MutableLiveData<AppState> = MutableLiveData(),
//    private val repositoryImpl: RepositoryImpl = RepositoryImpl()
) :
    ViewModel() {

    fun getLiveData(): LiveData<AppState> {
        return liveData
    }

//    private val _text = MutableLiveData<String>().apply {
//        value = "This is home Fragment"
//    }
//
//    val text: LiveData<String> = _text

    fun getWeather() {
//        скоро будет какой то переключатель
//        repositoryImpl.getWeatherFromServer()
        Thread {
            Thread.sleep(1000)
            liveData.postValue(AppState.Error(IllegalAccessError("Error")))
            Thread.sleep(3000)
            liveData.postValue(AppState.Loading(0))
            liveData.postValue(AppState.Loading(50))
            Thread.sleep(3000)
            liveData.postValue(AppState.Loading(100))
            val rand = (-40..40).random()
            when {
                rand < -15 -> {
                    postValue("$rand", "очень холодно")
                }
                rand < -10 -> {
                    postValue("$rand", "холодно")
                }
                rand > 30 -> {
                    postValue("+$rand", "очень жарко")
                }
                rand > 22 -> {
                    postValue("+$rand", "жарко")
                }
                rand > 10 -> {
                    postValue("+$rand", "тепло")
                }
                rand > 0 -> {
                    postValue("+$rand", "тепло")
                }
                else -> {
                    postValue("$rand", "прохладно")
                }
            }
        }.start()
    }

    private fun postValue(rand: String, feelsLike: String) {
        liveData.postValue(
            AppState.Success(
//                repositoryImpl.getWeatherFromServer()
                Weather(
                    temperature = rand,
                    feelsLike = feelsLike
                )
            )
        )

    }
}