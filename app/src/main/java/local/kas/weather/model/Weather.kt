package local.kas.weather.model

data class Weather(
    val city: City = getRandomCity(),
    val temperature: String = "0",
    val feelsLike: String = "прохладно"
)