package local.kas.weather.viewmodel

sealed class AppState {
    data class Loading(val progress: Int) : AppState()
    data class Success(val weatherData: String, val feelingWeather: String) : AppState()
    data class Error(val error: Throwable) : AppState()
//    LOADING(),
//    SUCCESS(),
//    ERROR()
}