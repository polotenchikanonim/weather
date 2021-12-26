package local.kas.weather.model

interface Repository {

    fun getWeatherFromServer(): Weather
    fun getWeatherLocal(): Weather

}