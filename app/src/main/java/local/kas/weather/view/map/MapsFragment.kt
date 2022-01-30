package local.kas.weather.view.map

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Geocoder
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import local.kas.weather.R
import local.kas.weather.databinding.FragmentMapsBinding
import local.kas.weather.model.City
import local.kas.weather.utils.BUNDLE_KEY
import java.util.*

class MapsFragment : Fragment() {

    private var _binding: FragmentMapsBinding? = null
    private val binding get() = _binding!!


    private var map: GoogleMap? = null
    private val markers = arrayListOf<Marker>()

    private val callback = OnMapReadyCallback { googleMap ->
        if (map == null) {
            map = getMap(googleMap)  //googleMap
        }

        map?.setOnMapClickListener {
            addMarker(it)
            drawLine()
        }
        googleMap.setOnMapLongClickListener {
            showWeather(it)
        }

    }

    private fun showWeather(latLng: LatLng) {
        requireActivity().let {
            val navController = it.findNavController(R.id.container)
            it.runOnUiThread {
                navController.navigate(R.id.nav_weather, Bundle().apply {
                    putParcelable(
                        BUNDLE_KEY,
                        City("cityName", latLng.latitude, latLng.longitude)
                    )
                })
            }
        }
    }

    private fun getMap(googleMap: GoogleMap): GoogleMap? {
        map = googleMap
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            Toast.makeText(requireContext(), "needPermissions", Toast.LENGTH_LONG).show()
        }
        googleMap.isMyLocationEnabled = true
        googleMap.uiSettings.isZoomControlsEnabled = true
        return map

    }

    private fun drawLine() {
        val last = markers.size
        if (last > 1) {
            map?.addPolyline(
                PolylineOptions().add(
                    markers[last - 1].position,
                    markers[last - 2].position
                ).color(Color.RED)
                    .width(5f)
            )
        }
    }

    private fun addMarker(location: LatLng) {
        map?.addMarker(
            MarkerOptions().position(location)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_action_name))
        )?.let { markers.add(it) }

    }


    private fun getAddress(latLng: LatLng) { // у мну нет apikey
        Geocoder(requireContext(), Locale.getDefault()).let {
            with(latLng) {
                Thread {
                    val addresses = it.getFromLocation(latitude, longitude, 1)
                    if (addresses.size > 0) {
                        val cityName = addresses[0].getAddressLine(0)
                        requireActivity().run {
                            runOnUiThread {
                                Toast.makeText(this, cityName, Toast.LENGTH_LONG).show()
                            }
                        }
                    }
                }.start()
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMapsBinding.inflate(inflater, container, false)
        setListeners()
        return binding.root
    }

    private fun setListeners() {
        binding.request.setOnClickListener {
            search()
        }
    }

    private fun search() { // у мну нет apikey
        Thread {
            Geocoder(requireContext()).let {
                val addresses = it.getFromLocationName(binding.searchAddress.text.toString(), 1)
                LatLng(addresses[0].latitude, addresses[0].longitude).run {
                    val cameraUpdateFactory = CameraUpdateFactory.newLatLngZoom(this, 10f)
                    requireActivity().runOnUiThread {
                        map?.moveCamera(cameraUpdateFactory)
                        addMarker(this)
                    }
                }
            }
        }.start()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
    }
}