package local.kas.weather.view.weather

import local.kas.weather.model.Weather

interface OnItemClickListener {
    fun onItemClick(weather: Weather)
}