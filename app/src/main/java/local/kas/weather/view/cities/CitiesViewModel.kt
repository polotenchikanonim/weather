package local.kas.weather.view.cities

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import local.kas.weather.repository.local.RepositoryLocalImpl
import local.kas.weather.viewmodel.CitiesAppState


class CitiesViewModel(private val liveDataToObserve: MutableLiveData<CitiesAppState> = MutableLiveData()) :
    ViewModel() {

    private val repositoryLocalImpl: RepositoryLocalImpl by lazy {
        RepositoryLocalImpl()
    }

    fun getLiveData() = liveDataToObserve

    fun getWeatherFromLocalSourceRus() = getDataFromLocalSource(isRussian = true)

    fun getWeatherFromLocalSourceWorld() = getDataFromLocalSource(isRussian = false)

    private fun getDataFromLocalSource(isRussian: Boolean) {
        liveDataToObserve.postValue(
            CitiesAppState.Success(
                if (isRussian) {
                    repositoryLocalImpl.getWeatherFromLocalStorageRus()
                } else {
                    repositoryLocalImpl.getWeatherFromLocalStorageWorld()
                }
            )
        )
    }

}