package local.kas.weather.model

data class City(val name: String, val latitude: Double, val longitude: Double)

fun getRandomCity() = City("Moscow", 55.7522, 37.6156)