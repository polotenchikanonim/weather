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
import android.view.View
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import local.kas.weather.R
import local.kas.weather.databinding.FragmentCitiesBinding
import local.kas.weather.model.City
import local.kas.weather.utils.BUNDLE_KEY
import local.kas.weather.utils.BaseFragment
import local.kas.weather.viewmodel.CitiesAppState
import java.util.*


private const val MIN_DISTANCE = 100f
private const val REFRESH_PERIOD = 60000L
private const val IS_WORLD_KEY = "LIST_OF_TOWNS_KEY"

class CitiesFragment : BaseFragment<FragmentCitiesBinding>(FragmentCitiesBinding::inflate),
    OnItemClickListener {

    private var isRussian = true

    private val citiesViewModel: CitiesViewModel by lazy {
        ViewModelProvider(this).get(CitiesViewModel::class.java)
    }

    private val recyclerViewAdapter: RecyclerViewAdapter by lazy {
        RecyclerViewAdapter(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initView()
        citiesViewModel.getLiveData().observe(viewLifecycleOwner) { renderData(it) }
        citiesViewModel.getWeatherFromLocalSourceRus()
        showListOfTowns()
    }

    private fun showListOfTowns() {
        requireActivity().let {
            if (it.getPreferences(Context.MODE_PRIVATE).getBoolean(IS_WORLD_KEY, false)) {
                changeWeatherDataSet()
            } else {
                citiesViewModel.getWeatherFromLocalSourceRus()
            }
        }
    }

    private fun changeWeatherDataSet() {
        isRussian = !isRussian
        if (isRussian) {
            citiesViewModel.getWeatherFromLocalSourceRus()
        } else {
            citiesViewModel.getWeatherFromLocalSourceWorld()
        }
        saveListOfTowns(!isRussian)
    }

    private fun saveListOfTowns(isDataSetWorld: Boolean) {
        activity?.let {
            with(it.getPreferences(Context.MODE_PRIVATE).edit()) {
                putBoolean(IS_WORLD_KEY, isDataSetWorld)
                apply()
            }
        }
    }

    private fun initView() {
        with(binding) {
            mainFragmentRecyclerView.adapter = recyclerViewAdapter
            changeCitiesButton.setOnClickListener { changeWeatherDataSet() }
            buttonGetLocalWeather.setOnClickListener { checkPermission() }
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

    override fun onItemClick(city: City) {
        showWeather(city)
    }

    private fun showWeather(city: City) {
        val navController = requireActivity().findNavController(R.id.container)
        navController.navigate(R.id.nav_weather, Bundle().apply { putParcelable(BUNDLE_KEY, city) })
    }

    private fun getLocation() {
        (requireContext().getSystemService(Context.LOCATION_SERVICE) as LocationManager).run {
            if (isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                getProvider(LocationManager.GPS_PROVIDER)?.let {
                    if (ActivityCompat.checkSelfPermission(
                            requireContext(), Manifest.permission.ACCESS_FINE_LOCATION
                        ) != PackageManager.PERMISSION_GRANTED
                    ) {
                        return
                    }
                }
                requestLocationUpdates(
                    LocationManager.GPS_PROVIDER, REFRESH_PERIOD, MIN_DISTANCE, locationListener
                )
            } else {
                getLastKnownLocation(LocationManager.GPS_PROVIDER)?.let { location ->
                    getAddress(location)
                }
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
        requireContext().run {
            when {
                ContextCompat.checkSelfPermission(
                    this, Manifest.permission.ACCESS_FINE_LOCATION
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
        AlertDialog.Builder(requireContext()).apply {
            setTitle("Доступ к geo").setMessage("message")
            setPositiveButton("предоставить доступ") { _, _ -> requestPermission() }
            setNegativeButton("no") { dialog, _ -> dialog.dismiss() }
            create().show()
        }
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