package local.kas.weather.view.weather

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import local.kas.weather.databinding.FragmentDetailsBinding
import local.kas.weather.model.Weather
import local.kas.weather.model.WeatherDTO


const val BUNDLE_KEY = "BUNDLE_KEY"
const val BUNDLE_KEY_WEATHER = "BUNDLE_KEY_WEATHER"
const val BROADCAST_ACTION = "BROADCAST_ACTION"

const val BUNDLE_KEY_LAT = "BUNDLE_KEY_LAT"
const val BUNDLE_KEY_LON = "BUNDLE_KEY_LON"


class DetailsFragment : Fragment() {

    private var _binding: FragmentDetailsBinding? = null
    private val binding get() = _binding!!


    private val myBroadcastReceiver2: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            intent?.let {
                it.getParcelableExtra<WeatherDTO>(BUNDLE_KEY_WEATHER)?.let {
                    setWeather(it)
                }
            }
        }
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.let { it ->
            it.getParcelable<Weather>(BUNDLE_KEY)?.let {
                binding.cityName.text = it.city.name
                val coordinates =
                    "${it.city.latitude} ${it.city.longitude}"
                binding.cityCoordinates.text = coordinates

                requireActivity().startService(
                    Intent(
                        requireActivity(),
                        WeatherService::class.java
                    ).apply {
                        putExtra(BUNDLE_KEY_LAT, it.city.latitude)
                        putExtra(BUNDLE_KEY_LON, it.city.longitude)
                    })
            }
        }
        LocalBroadcastManager.getInstance(requireContext())
            .registerReceiver(myBroadcastReceiver2, IntentFilter(BROADCAST_ACTION))
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    private fun setWeather(weatherDTO: WeatherDTO) {
        LocalBroadcastManager.getInstance(requireContext()).unregisterReceiver(myBroadcastReceiver2)
        with(binding) {
            with(weatherDTO) {
                temperatureValue.text = temp.toString()
                feelsLikeValue.text = feelsLike.toString()
            }
        }
    }
}