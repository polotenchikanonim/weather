package local.kas.weather.view.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import local.kas.weather.R
import local.kas.weather.databinding.FragmentDetailsBinding
import local.kas.weather.model.Weather
import local.kas.weather.viewmodel.WeatherAppState


const val BUNDLE_KEY = "key"

class DetailsFragment : Fragment() {


    private lateinit var detailsViewModel: WeatherViewModel
    private var _binding: FragmentDetailsBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        detailsViewModel = ViewModelProvider(this).get(WeatherViewModel::class.java)


        detailsViewModel.getLiveData().observe(viewLifecycleOwner, { renderData(it) })
        arguments?.let { it ->
            it.getParcelable<Weather>(BUNDLE_KEY)?.let {
                detailsViewModel.loadWeather(it.city)
            }
        }
    }

    private fun setWeatherData(weatherAppState: WeatherAppState.Success) {
        with(binding) {
            with(weatherAppState) {
                cityName.text = city.name
                val coordinates =
                    "${weatherAppState.city.latitude} ${weatherAppState.city.longitude}"
                cityCoordinates.text = coordinates
                temperatureValue.text = "${weatherAppState.weatherData.temp}"
                feelsLikeValue.text = "${weatherAppState.weatherData.feelsLike}"
            }
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }


    private fun renderData(weatherAppState: WeatherAppState) {
        when (weatherAppState) {
            is WeatherAppState.Error -> {
                Toast.makeText(
                    requireContext(),
                    getString(R.string.city_coordinates),
                    Toast.LENGTH_SHORT
                ).show()

            }

            is WeatherAppState.Success -> {
                setWeatherData(weatherAppState)
            }
            is WeatherAppState.Loading -> {
                println()
                Toast.makeText(requireContext(), "грузим", Toast.LENGTH_SHORT).show()
            }
        }
    }
}