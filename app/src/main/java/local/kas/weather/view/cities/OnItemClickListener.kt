package local.kas.weather.view.cities

import local.kas.weather.model.Weather

interface OnItemClickListener {
    fun onItemClick(weather: Weather)
}