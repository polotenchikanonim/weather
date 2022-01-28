package local.kas.weather.view.history

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import local.kas.weather.databinding.FragmentHistoryBinding
import local.kas.weather.model.WeatherHistory
import local.kas.weather.viewmodel.AppStateDB
import local.kas.weather.viewmodel.HistoryViewModel

class HistoryFragment : Fragment(), OnMyClickListener {

    private var _binding: FragmentHistoryBinding? = null
    private val binding get() = _binding!!

    private val recyclerViewAdapter: HistoryRecyclerViewAdapter by lazy {
        HistoryRecyclerViewAdapter(this)
    }

    private val viewModel: HistoryViewModel by lazy {
        ViewModelProvider(this).get(HistoryViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHistoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getLiveData().observe(viewLifecycleOwner, { renderData(it) })
        viewModel.getAllHistory()
        binding.list.adapter = recyclerViewAdapter
    }

    private fun renderData(appStateDB: AppStateDB) {
        when (appStateDB) {
            is AppStateDB.Success -> {
                recyclerViewAdapter.setWeather(appStateDB.weatherData)
            }
        }
    }

    override fun onClick(weatherHistory: WeatherHistory) {
        with(weatherHistory) {
            val output = "$cityName $temperature $feelsLike"
            Toast.makeText(requireContext(), output, Toast.LENGTH_LONG).show()
        }
    }

}