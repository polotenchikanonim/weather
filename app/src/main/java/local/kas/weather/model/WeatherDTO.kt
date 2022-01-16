package local.kas.weather.model

data class WeatherDTO(
//    val fact: Fact
    val temp: Int,
    val feelsLike: Int
)

data class Fact(
    val temp: Int,
    val feelsLike: Int
)