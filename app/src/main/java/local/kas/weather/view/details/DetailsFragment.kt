package local.kas.weather.view.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import local.kas.weather.R
import local.kas.weather.databinding.FragmentDetailsBinding
import local.kas.weather.model.Weather


const val BUNDLE_KEY = "key"

class DetailsFragment : Fragment() {


    private lateinit var detailsViewModel: DetailsViewModel

    private var _binding: FragmentDetailsBinding? = null
    private val binding get() = _binding!!


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        detailsViewModel = ViewModelProvider(this).get(DetailsViewModel::class.java)

        val textView: TextView = binding.cityName

        detailsViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })

        val weather = arguments?.getParcelable<Weather>(BUNDLE_KEY)
        if (weather != null) {
            setWeatherData(weather)
        }
    }

    private fun setWeatherData(weather: Weather) {
        detailsViewModel.text.observe(viewLifecycleOwner, {
            binding.cityName.text = weather.city.name
            binding.cityCoordinates.text = String.format(
                getString(R.string.city_coordinates),
                weather.city.latitude,
                weather.city.longitude
            )
            binding.temperatureValue.text = weather.temperature.toString()
            binding.feelsLikeValue.text = weather.feelsLike.toString()
        })
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