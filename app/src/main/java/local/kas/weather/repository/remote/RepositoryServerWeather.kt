package local.kas.weather.repository.remote

interface RepositoryServerWeather {
    //    fun getWeatherFromServerRetrofit(lat: Double, lon: Double, callback: Callback<WeatherDTO>)
    fun getWeatherFromServerHttp(lat: Double, lon: Double, callbackHttp: okhttp3.Callback)
}