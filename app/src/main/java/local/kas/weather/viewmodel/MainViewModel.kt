package local.kas.weather.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.lang.Thread.sleep

class MainViewModel(private val liveData: MutableLiveData<AppState> = MutableLiveData()) :
    ViewModel() {

    fun getLiveData(): LiveData<AppState> {
        return liveData
    }

    private val _text = MutableLiveData<String>().apply {
        value = "This is home Fragment"
    }

    val text: LiveData<String> = _text

    fun getWeatherFromServer() {
        Thread {
            liveData.postValue(AppState.Loading(0))
            sleep(5000)
            liveData.postValue(AppState.Loading(50))
            sleep(3000)
            liveData.postValue(AppState.Loading(100))
            liveData.postValue(AppState.Success("Холодно", "Очень холодно"))  // synchronous
        }.start()
    }
}