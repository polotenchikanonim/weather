package local.kas.weather.view.details

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import local.kas.weather.R
import local.kas.weather.databinding.FragmentDetailsBinding
import local.kas.weather.model.Weather

class DetailsViewModel : ViewModel() {
    fun renderData(binding: FragmentDetailsBinding, weather: Weather) {
        with(binding) {

            cityName.text = weather.city.name
            cityCoordinates.text = String.format(
                mainView.context.resources.getString(R.string.city_coordinates), // не возвращает
                weather.city.latitude,
                weather.city.longitude
            )
            temperatureValue.text = weather.temperature.toString()
            feelsLikeValue.text = weather.feelsLike.toString()
        }
    }

    private val _text = MutableLiveData<String>().apply {
        value = "This is gallery Fragment"
    }
    val text: LiveData<String> = _text


}