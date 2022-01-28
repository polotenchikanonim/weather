package local.kas.weather.view.weather

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import coil.ImageLoader
import coil.decode.SvgDecoder
import coil.request.ImageRequest
import com.bumptech.glide.Glide
import local.kas.weather.R
import local.kas.weather.databinding.FragmentWeatherBinding

import local.kas.weather.model.City
import local.kas.weather.model.WeatherDTO
import local.kas.weather.model.WeatherHistory
import local.kas.weather.utils.BUNDLE_KEY
import local.kas.weather.viewmodel.WeatherAppState
import local.kas.weather.viewmodel.WeatherViewModel


class WeatherFragment : Fragment() {

    private var _binding: FragmentWeatherBinding? = null
    private val binding get() = _binding!!

    private val viewModel: WeatherViewModel by lazy {
        ViewModelProvider(this).get(WeatherViewModel::class.java)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getLiveData().observe(viewLifecycleOwner, {
            renderData(it)
        })
        arguments?.let { it ->
            it.getParcelable<City>(BUNDLE_KEY)?.let {
                binding.cityName.text = it.name
                val coordinates = "${it.lat} ${it.lon}"
                binding.cityCoordinates.text = coordinates
                viewModel.getWeatherFromServer(it.lat, it.lon)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentWeatherBinding.inflate(inflater, container, false)
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
                setWeather(weatherAppState.weatherData)
            }
            is WeatherAppState.Loading -> {
                Toast.makeText(requireContext(), "грузим", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setWeather(weatherDTO: WeatherDTO) {
        with(binding) {
            with(weatherDTO) {
                Glide.with(headerIcon.context)
                    .load("https://freepngimg.com/thumb/city/36275-3-city-hd.png")
                    .into(headerIcon)
                temperatureValue.text = temp.toString()
                feelsLikeValue.text = feelsLike.toString()
                iconWeather.loadUrl("https://yastatic.net/weather/i/icons/funky/dark/$icon.svg")
                viewModel.saveWeather(
                    WeatherHistory(
                        binding.cityName.text.toString(),
                        temp,
                        feelsLike,
                        "https://yastatic.net/weather/i/icons/funky/dark/$icon.svg"
                    )
                )
            }
        }
    }

    private fun ImageView.loadUrl(url: String) {
        val imageLoader = ImageLoader.Builder(this.context)
            .componentRegistry { add(SvgDecoder(this@loadUrl.context)) }
            .build()
        val request = ImageRequest.Builder(this.context)
            .data(url)
            .target(this)
            .build()
        imageLoader.enqueue(request)
    }

}