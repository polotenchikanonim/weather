package local.kas.weather.view.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import local.kas.weather.R
import local.kas.weather.databinding.FragmentDetailsBinding
import local.kas.weather.model.Weather


const val BUNDLE_KEY = "key"

class DetailsFragment : Fragment() {

    private var _binding: FragmentDetailsBinding? = null
    private val binding get() = _binding!!


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val weather = arguments?.getParcelable<Weather>(BUNDLE_KEY)
        if (weather != null) {
            setWeatherData(weather)
        }
    }

    private fun setWeatherData(weather: Weather) {
        binding.cityName.text = weather.city.name
        binding.cityCoordinates.text = String.format(
            getString(R.string.city_coordinates),
            weather.city.latitude,
            weather.city.longitude
        )
        binding.temperatureValue.text = weather.temperature.toString()
        binding.feelsLikeValue.text = weather.feelsLike.toString()
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }


    companion object {



        fun newInstance(bundle: Bundle): DetailsFragment {
            val fragment = DetailsFragment()
            fragment.arguments = bundle
            return fragment
        }
    }
}