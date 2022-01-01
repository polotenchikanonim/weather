package local.kas.weather.view.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
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

        arguments?.run { // а так можно, или это плохо? let
            getParcelable<Weather>(BUNDLE_KEY)?.run {
                detailsViewModel.renderData(binding, this)
                // че то я тут понаписал хотел чтоб viewModel учавствовала help please best practice
            }
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }


}