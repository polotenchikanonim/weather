package local.kas.weather.view.weather

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import local.kas.weather.model.Repository
import local.kas.weather.model.RepositoryImpl
import local.kas.weather.viewmodel.AppState
import java.lang.Thread.sleep


class WeatherViewModel(
    private val liveDataToObserve: MutableLiveData<AppState> = MutableLiveData(),
    private val repositoryImpl: Repository = RepositoryImpl()
) :
    ViewModel() {

    fun getLiveData() = liveDataToObserve

    fun getWeatherFromLocalSourceRus() = getDataFromLocalSource(isRussian = true)

    fun getWeatherFromLocalSourceWorld() = getDataFromLocalSource(isRussian = false)

    fun getWeatherFromRemoteSource() = getDataFromLocalSource(isRussian = true)

    private fun getDataFromLocalSource(isRussian: Boolean) {
        liveDataToObserve.value = AppState.Loading(0)
        Thread {
            sleep(1000)
            liveDataToObserve.postValue(
                AppState.Success(
                    if (isRussian) {
                        repositoryImpl.getWeatherFromLocalStorageRus()
                    }
                    else {
                        repositoryImpl.getWeatherFromLocalStorageWorld()
                    }
                )
            )
        }.start()
    }
}