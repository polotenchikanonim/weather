package local.kas.weather.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import local.kas.weather.repository.local.RepositoryLocalImpl

class HistoryViewModel(
    private val liveData: MutableLiveData<AppStateDB> = MutableLiveData()
) : ViewModel() {

    private val repositoryLocalImpl: RepositoryLocalImpl by lazy {
        RepositoryLocalImpl()
    }

    fun getLiveData() = liveData

    fun getAllHistory() {
        Thread {
            val listWeather = repositoryLocalImpl.getAllHistoryWeather()
            liveData.postValue(AppStateDB.Success(listWeather))
        }.start()

    }
}