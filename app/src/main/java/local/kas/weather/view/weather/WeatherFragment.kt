package local.kas.weather.view.weather

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import local.kas.weather.R
import local.kas.weather.databinding.FragmentWeatherBinding
import local.kas.weather.model.Weather
import local.kas.weather.view.details.BUNDLE_KEY

import local.kas.weather.view.details.DetailsFragment

import local.kas.weather.viewmodel.AppState


class WeatherFragment : Fragment(), OnItemClickListener {

    private lateinit var weatherViewModel: WeatherViewModel

    private var _binding: FragmentWeatherBinding? = null

    private val binding get() = _binding!!

    private var recyclerViewAdapter = RecyclerViewAdapter(this)
    private var isRussian = true


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        weatherViewModel = ViewModelProvider(this).get(WeatherViewModel::class.java)
        _binding = FragmentWeatherBinding.inflate(inflater, container, false)
        weatherViewModel.getLiveData().observe(viewLifecycleOwner, { renderData(it) })
        binding.mainFragmentRecyclerView.adapter = recyclerViewAdapter
        binding.mainFragmentFAB.setOnClickListener {
            request()
        }
        weatherViewModel.getWeatherFromLocalSourceRus()

        return binding.root
    }

    private fun request() {
        if (isRussian) {
            weatherViewModel.getWeatherFromLocalSourceRus()
        } else {
            weatherViewModel.getWeatherFromLocalSourceWorld()
        }
        isRussian = !isRussian
    }


    private fun renderData(appState: AppState) {
        if (binding.weatherFragmentLoadingLayout.visibility == View.VISIBLE) {
            binding.weatherFragmentLoadingLayout.visibility = View.GONE
        }
        when (appState) {
            is AppState.Error -> {
                showErrorSnack(appState.error.message.toString())
            }
            is AppState.Loading -> binding.weatherFragmentLoadingLayout.visibility = View.VISIBLE
            is AppState.Success -> {
                showWeather(appState)
            }
        }
    }

    private fun showWeather(appState: AppState.Success) {
        recyclerViewAdapter.setWeather(appState.weatherData)
//        binding.cityName.text = appState.weatherData.city.name
//        binding.cityCoordinates.text = String.format(
//            getString(R.string.city_coordinates),
//            appState.weatherData.city.latitude,
//            appState.weatherData.city.longitude
//        )
//        binding.temperatureValue.text = appState.weatherData.temperature.toString()
//        binding.feelsLikeValue.text = appState.weatherData.feelsLike.toString()
    }

    private fun showErrorSnack(snack: String) {
        Snackbar.make(
            binding.root, snack, Snackbar.LENGTH_LONG
        ).setAction("to try one more time") {
            request()
        }.show()
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onItemClick(weather: Weather) {

        val bundle = Bundle()
        bundle.putParcelable(BUNDLE_KEY, weather)
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.container, DetailsFragment.newInstance(bundle))
//                .add(R.id.container, DetailsFragment.newInstance(bundle))
            .addToBackStack(null).commit()


    }

}