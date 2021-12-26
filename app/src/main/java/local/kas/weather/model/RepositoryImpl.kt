package local.kas.weather.model

class RepositoryImpl : Repository {

    override fun getWeatherFromServer(): Weather {
        return Weather()
    }

    override fun getWeatherLocal(): Weather {
        return Weather()
    }

}