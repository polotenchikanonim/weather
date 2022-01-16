package local.kas.weather.view.weather

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import local.kas.weather.model.RepositoryImpl
import local.kas.weather.viewmodel.CitiesAppState


class CitiesViewModel(private val liveDataToObserve: MutableLiveData<CitiesAppState> = MutableLiveData()) :
    ViewModel() {

    private val repositoryImpl: RepositoryImpl by lazy {
        RepositoryImpl()
    }

    fun getLiveData() = liveDataToObserve

    fun getWeatherFromLocalSourceRus() = getDataFromLocalSource(isRussian = true)

    fun getWeatherFromLocalSourceWorld() = getDataFromLocalSource(isRussian = false)

    private fun getDataFromLocalSource(isRussian: Boolean) {
        liveDataToObserve.value = CitiesAppState.Loading(0)

        liveDataToObserve.postValue(
            CitiesAppState.Success(
                if (isRussian) {
                    repositoryImpl.getWeatherFromLocalStorageRus()
                } else {
                    repositoryImpl.getWeatherFromLocalStorageWorld()
                }
            )
        )
    }
}