package local.kas.weather.view.history

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageView
import androidx.recyclerview.widget.RecyclerView
import coil.ImageLoader
import coil.decode.SvgDecoder
import coil.request.ImageRequest
import local.kas.weather.databinding.FragmentItemBinding
import local.kas.weather.model.WeatherHistory


class HistoryRecyclerViewAdapter(private val onMyClickListener: OnMyClickListener) :
    RecyclerView.Adapter<HistoryRecyclerViewAdapter.ViewHolder>() {

    private var historyWeatherData: List<WeatherHistory> = listOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            FragmentItemBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    fun setWeather(data: List<WeatherHistory>) {
        historyWeatherData = data
        notifyDataSetChanged() // fixme please. Sorry i don't know
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(historyWeatherData[position]) {
            holder.cityName.text = cityName
            holder.temperature.text = temperature.toString()
            holder.feelsLike.text = feelsLike.toString()
            holder.loadIcon()
//            holder.icon.loadUrl("https://yastatic.net/weather/i/icons/funky/dark/$icon.svg")
//            holder.itemView.setOnClickListener {
//                onMyClickListener.onClick(this)
//            }
        }
    }

    override fun getItemCount(): Int = historyWeatherData.size

    inner class ViewHolder(binding: FragmentItemBinding) : RecyclerView.ViewHolder(binding.root) {
        val cityName: TextView = binding.cityName
        val temperature: TextView = binding.temperature
        val feelsLike: TextView = binding.feelsLike
        val icon: AppCompatImageView = binding.icon

        fun loadIcon() {
            icon.loadUrl("https://yastatic.net/weather/i/icons/funky/dark/$icon.svg")
        }
    }

    private fun ImageView.loadUrl(url: String) {
        val imageLoader = ImageLoader.Builder(this.context)
            .componentRegistry { add(SvgDecoder(this@loadUrl.context)) }
            .build()
        val request = ImageRequest.Builder(this.context)
            .data(url)
            .target(this)
            .build()
        imageLoader.enqueue(request)
    }

}