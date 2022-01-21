package local.kas.weather.view.cities

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.google.android.material.snackbar.Snackbar
import local.kas.weather.R
import local.kas.weather.databinding.FragmentWeatherBinding
import local.kas.weather.model.Weather
import local.kas.weather.view.weather.BUNDLE_KEY
import local.kas.weather.viewmodel.CitiesAppState


class CitiesFragment : Fragment(), OnItemClickListener {

    private var isRussian = true
    private lateinit var weatherViewModel: CitiesViewModel
    private var _binding: FragmentWeatherBinding? = null
    private val binding get() = _binding!!

    private val recyclerViewAdapter: RecyclerViewAdapter by lazy {
        RecyclerViewAdapter(this)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        weatherViewModel = ViewModelProvider(this).get(CitiesViewModel::class.java)
        _binding = FragmentWeatherBinding.inflate(inflater, container, false)
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
                is CitiesAppState.Error -> {
                    getString(R.string.city_coordinates)
                    binding.root.showErrorSnack(R.string.city_coordinates)
                }
                is CitiesAppState.Loading -> weatherFragmentLoadingLayout.visibility = View.VISIBLE
                is CitiesAppState.Success -> {
                    showCities(citiesAppState)
                }
            }
        }
    }

    private fun showCities(citiesAppState: CitiesAppState.Success) {
        recyclerViewAdapter.setWeather(citiesAppState.weatherData)
    }

    private fun View.showErrorSnack(snack: Int) {
        Snackbar.make(
            this, getString(snack), Snackbar.LENGTH_LONG
        ).setAction(getString(R.string.try_one_more_time)) {
            request()
        }.show()
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    override fun onItemClick(weather: Weather) {
        val navController = requireActivity().findNavController(R.id.container)
        navController.navigate(
            R.id.nav_details,
            Bundle().apply { putParcelable(BUNDLE_KEY, weather) }
        )
    }
}