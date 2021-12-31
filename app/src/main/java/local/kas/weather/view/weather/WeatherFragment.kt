package local.kas.weather.view.weather

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import local.kas.weather.databinding.FragmentHomeBinding
import local.kas.weather.viewmodel.AppState


class WeatherFragment : Fragment() {

    private lateinit var weatherViewModel: WeatherViewModel
    private var _binding: FragmentHomeBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        weatherViewModel = ViewModelProvider(this).get(WeatherViewModel::class.java)
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        weatherViewModel.getLiveData().observe(viewLifecycleOwner, { renderData(it) })
        weatherViewModel.getWeather()
        initView()
        return binding.root
    }

    private fun initView() {

    }

    private fun renderData(appState: AppState) {
        if (binding.loadingLayout.visibility == View.VISIBLE) {
            binding.loadingLayout.visibility = View.GONE
        }
        when (appState) {
            is AppState.Error -> {
                showErrorSnack(appState.error.message.toString())
            }
            is AppState.Loading -> binding.loadingLayout.visibility = View.VISIBLE
            is AppState.Success -> {
                showWeather(appState)
            }
        }
    }

    private fun showWeather(appState: AppState.Success) {
        binding.cityName.text = appState.weatherData.city.name

//        binding.cityCoordinates = String.format(  // почему то не работает
//            getString(R.string.city_coordinates),
//            appState.weatherData.city.latitude,
//            appState.weatherData.city.longitude
//        )

        val latLon =
            "lt/ln: ${appState.weatherData.city.latitude} ${appState.weatherData.city.longitude}"
        binding.cityCoordinates.text = latLon
        binding.temperatureValue.text = appState.weatherData.temperature
        binding.feelsLikeValue.text = appState.weatherData.feelsLike
    }

    private fun showErrorSnack(snack: String) {
        Snackbar.make(
            binding.mainView, snack, Snackbar.LENGTH_LONG
        ).setAction("to try one more time") {
            weatherViewModel.getWeather()
        }.show()
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}