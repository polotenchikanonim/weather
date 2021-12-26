package local.kas.weather.view.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import local.kas.weather.Person
import local.kas.weather.databinding.FragmentHomeBinding
import local.kas.weather.viewmodel.AppState
import local.kas.weather.viewmodel.MainViewModel

class HomeFragment : Fragment() {

    //    private lateinit var homeViewModel: HomeViewModel

    private lateinit var mainViewModel: MainViewModel
    private var _binding: FragmentHomeBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        mainViewModel = ViewModelProvider(this).get(MainViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        mainViewModel.getLiveData()
            .observe(viewLifecycleOwner, Observer<AppState> { renderData(it) })
        mainViewModel.getWeatherFromServer()
//        val root: View = binding.root
        initView()
        work()

        return binding.root
    }

    private fun work() {
//        a. Сформировать data class с двумя свойствами и вывести их на экран приложения.
        val person = Person("polotenchik", "towel", 42)
        val user = person.firstName + " " + person.age
        Toast.makeText(requireContext(), user, Toast.LENGTH_LONG).show()

//        b. Создать Object. В Object вызвать copy и вывести значения скопированного класса на экран.
//        Repository.getData()
        val admin = person.copy()
        println(admin)

//        c. Вывести значения из разных циклов в консоль, используя примеры из методических материалов.
        for (i in 1..10) {
            Log.d(TAG, "$i")
        }

        for (i in 10 downTo 1 step 2) {
            Log.d(TAG, "$i")
        }

        for (i in 0 until 19) {
            Log.d(TAG, "$i")
        }

        repeat(20) {
            Log.d(TAG, "$it")
        }

        val daysOfWeek = listOf("sunday", "monday", "tuesday", "wednesday")
        daysOfWeek.forEach {
            Log.d(TAG, "$it $TAG")
        }

        for (day in daysOfWeek) {
            Log.d(TAG, day)
        }

    }

    private fun initView() {
//        4. Добавить кнопку в разметку и повесить на неё clickListener в Activity.
//        val textView: TextView = binding.textHome  // интересное место

        val button = binding.textHome
        mainViewModel.text.observe(viewLifecycleOwner, {
            button.text = it
        })

        button.setOnClickListener {
            println(123)
            Toast.makeText(button.context, button.text, Toast.LENGTH_LONG)
                .show()
        }

    }

    private fun renderData(appState: AppState) {
        val context = requireContext()
        when (appState) {
            is AppState.Error -> Toast.makeText(
                context, appState.error.message, Toast.LENGTH_SHORT
            ).show()
            is AppState.Loading -> Toast.makeText(
                context, "${appState.progress}", Toast.LENGTH_SHORT
            ).show()
            is AppState.Success -> Toast.makeText(
                context, appState.weatherData+" "+appState.feelingWeather, Toast.LENGTH_SHORT
            ).show()
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val TAG = "MyActivity"
    }
}