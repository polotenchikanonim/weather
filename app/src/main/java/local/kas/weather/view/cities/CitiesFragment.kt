package local.kas.weather.view.cities

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import local.kas.weather.R
import local.kas.weather.databinding.FragmentCitiesBinding
import local.kas.weather.model.City
import local.kas.weather.utils.BUNDLE_KEY
import local.kas.weather.viewmodel.CitiesAppState


class CitiesFragment : Fragment(), OnItemClickListener {

    private var isRussian = true
    private lateinit var weatherViewModel: CitiesViewModel
    private var _binding: FragmentCitiesBinding? = null
    private val binding get() = _binding!!

    private val recyclerViewAdapter: RecyclerViewAdapter by lazy {
        RecyclerViewAdapter(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        weatherViewModel = ViewModelProvider(this).get(CitiesViewModel::class.java)
        _binding = FragmentCitiesBinding.inflate(inflater, container, false)
        initView()

        weatherViewModel.getLiveData().observe(viewLifecycleOwner, { renderData(it) })
        weatherViewModel.getWeatherFromLocalSourceRus()
        return binding.root
    }

    private fun initView() {
        with(binding) {
            mainFragmentRecyclerView.adapter = recyclerViewAdapter
            mainFragmentFAB.setOnClickListener {
                request()
            }
        }
    }

    private fun request() {
        isRussian = !isRussian
        if (isRussian) {
            weatherViewModel.getWeatherFromLocalSourceRus()
        } else {
            weatherViewModel.getWeatherFromLocalSourceWorld()
        }
    }


    private fun renderData(citiesAppState: CitiesAppState) {
        with(binding) {
            if (weatherFragmentLoadingLayout.visibility == View.VISIBLE) {
                weatherFragmentLoadingLayout.visibility = View.GONE
            }
            when (citiesAppState) {
                is CitiesAppState.Success -> {
                    showCities(citiesAppState)
                }
            }
        }
    }

    private fun showCities(citiesAppState: CitiesAppState.Success) {
        recyclerViewAdapter.setWeather(citiesAppState.weatherData)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onItemClick(city: City) {
        val navController = requireActivity().findNavController(R.id.container)
        navController.navigate(R.id.nav_weather, Bundle().apply { putParcelable(BUNDLE_KEY, city) })
    }
}