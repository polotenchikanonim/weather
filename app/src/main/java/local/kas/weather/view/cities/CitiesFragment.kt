package local.kas.weather.view.cities

import android.Manifest
import android.app.AlertDialog
import android.content.Context
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import local.kas.weather.R
import local.kas.weather.databinding.FragmentCitiesBinding
import local.kas.weather.model.City
import local.kas.weather.utils.BUNDLE_KEY
import local.kas.weather.viewmodel.CitiesAppState
import java.util.*

private const val MIN_DISTANCE = 100f
private const val REFRESH_PERIOD = 60000L

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
        weatherViewModel.getLiveData().observe(viewLifecycleOwner) { renderData(it) }
        weatherViewModel.getWeatherFromLocalSourceRus()
        return binding.root
    }

    private fun initView() {
        with(binding) {
            mainFragmentRecyclerView.adapter = recyclerViewAdapter
            mainFragmentFAB.setOnClickListener {
                request()
            }
            buttonGetLocalWeather.setOnClickListener {
                checkPermission()
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
        showWeather(city)
    }

    private fun showWeather(city: City) {
        val navController = requireActivity().findNavController(R.id.container)
        navController.navigate(R.id.nav_weather, Bundle().apply { putParcelable(BUNDLE_KEY, city) })
    }


    private fun getLocation() {
        val locationManager =
            requireContext().getSystemService(Context.LOCATION_SERVICE) as LocationManager
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            locationManager.getProvider(LocationManager.GPS_PROVIDER).let {
                if (ActivityCompat.checkSelfPermission(
                        requireContext(),
                        Manifest.permission.ACCESS_FINE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    return
                }
                locationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER, REFRESH_PERIOD,
                    MIN_DISTANCE, locationListener
                )

            }
        } else {
            locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)?.let {
                getAddress(it)
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<out String>, grantResults: IntArray
    ) {
        requireContext().let {
            when {
                ContextCompat.checkSelfPermission(
                    it, Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED -> {
                    getLocation()
                }
                shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION) -> {
                    showRatioDialog()
                }
                else -> {
                    println()
                }
            }
        }
    }

    private fun checkPermission() {
        requireContext().let {
            when {
                ContextCompat.checkSelfPermission(
                    it,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED -> {
                    getLocation()
                }
                shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION) -> {
                    showRatioDialog()
                }
                else -> {
                    requestPermission()
                }
            }
        }
    }

    private fun showRatioDialog() {
        AlertDialog.Builder(requireContext())
            .setTitle("Доступ к geo").setMessage("message")
            .setPositiveButton("предоставить доступ") { _, _ -> requestPermission() }
            .setNegativeButton("no") { dialog, _ -> dialog.dismiss() }
            .create().show()
    }

    private val locationListener = LocationListener {
        getAddress(it)
    }

    private fun requestPermission() {
        requestPermissions(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 1)
    }

    private fun getAddress(location: Location) {
        Geocoder(requireContext(), Locale.getDefault()).let {
            with(location) {
                Thread {
                    val addresses = it.getFromLocation(latitude, longitude, 1)
                    if (addresses.size > 0) {
                        val cityName = addresses[0].getAddressLine(0)
                        requireActivity().run {
                            runOnUiThread {
                                showWeather(City(cityName, latitude, longitude))
                            }
                        }
                    }
                }.start()
            }
        }
    }
}