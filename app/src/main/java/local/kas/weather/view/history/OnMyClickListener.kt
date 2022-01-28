package local.kas.weather.view.history

import local.kas.weather.model.WeatherHistory

interface OnMyClickListener {
    fun onClick(weatherHistory: WeatherHistory)
}