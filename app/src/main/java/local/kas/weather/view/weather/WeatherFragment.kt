package local.kas.weather.view.weather

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
import local.kas.weather.view.details.BUNDLE_KEY
import local.kas.weather.viewmodel.AppState


class WeatherFragment : Fragment(), OnItemClickListener {

    private lateinit var weatherViewModel: WeatherViewModel
    private var _binding: FragmentWeatherBinding? = null

    private val binding get() = _binding!!

    private val recyclerViewAdapter: RecyclerViewAdapter by lazy {
        RecyclerViewAdapter(this)
    }
    private var isRussian = true


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        weatherViewModel = ViewModelProvider(this).get(WeatherViewModel::class.java)
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


    private fun renderData(appState: AppState) {
        with(binding) {
            if (weatherFragmentLoadingLayout.visibility == View.VISIBLE) {
                weatherFragmentLoadingLayout.visibility = View.GONE
            }
            when (appState) {
                is AppState.Error -> {
                    getString(R.string.city_coordinates)
                    binding.root.showErrorSnack(R.string.city_coordinates)
                }
                is AppState.Loading -> weatherFragmentLoadingLayout.visibility = View.VISIBLE
                is AppState.Success -> {
                    showWeather(appState)
                }
            }
        }
    }

    private fun showWeather(appState: AppState.Success) {
        recyclerViewAdapter.setWeather(appState.weatherData)
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