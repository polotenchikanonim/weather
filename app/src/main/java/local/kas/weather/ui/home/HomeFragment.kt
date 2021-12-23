package local.kas.weather.ui.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import local.kas.weather.Person
import local.kas.weather.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {


    private val TAG: String = "myLogs"

    private lateinit var homeViewModel: HomeViewModel
    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root
        initView()
        work()
        return root
    }

    private fun work() {
//        a. Сформировать data class с двумя свойствами и вывести их на экран приложения.
        val person = Person("polotenchik", "towel", 42)
        val user = person.firstName + " " + person.age
        Toast.makeText(requireContext(), user, Toast.LENGTH_LONG).show()

//        b. Создать Object. В Object вызвать copy и вывести значения скопированного класса на экран.
//        Repository.getData()
        val admin = person.copy()


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

        val daysOfWeek = listOf("sunday", "monday", "tuesday", "wednesday");
        daysOfWeek.forEach {
            Log.d(TAG, it)
        }

        for (day in daysOfWeek) {
            Log.d(TAG, day)
        }

    }

    private fun initView() {
//        4. Добавить кнопку в разметку и повесить на неё clickListener в Activity.
        val textView: TextView = binding.textHome  // интересное место
        homeViewModel.text.observe(viewLifecycleOwner, {
            textView.text = it
        })

        textView.setOnClickListener {
            println(123)
            Toast.makeText(textView.context, textView.text, Toast.LENGTH_LONG).show()
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}