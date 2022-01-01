package local.kas.weather.view.weather

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import local.kas.weather.R
import local.kas.weather.model.Weather

class RecyclerViewAdapter(val listener: OnItemClickListener) :
    RecyclerView.Adapter<RecyclerViewAdapter.WeatherViewHolder>() {

    private var weatherData: List<Weather> = listOf()

    fun setWeather(data: List<Weather>) {
        weatherData = data
        notifyDataSetChanged()
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeatherViewHolder {
        return WeatherViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.city_recycler, parent, false)
        )
    }

    override fun onBindViewHolder(holder: WeatherViewHolder, position: Int) {
        holder.bind(this.weatherData[position])
    }


    override fun getItemCount(): Int {
        return weatherData.size
    }


    inner class WeatherViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(weather: Weather) {
            itemView.run {
                findViewById<AppCompatTextView>(R.id.weatherItemTextView).text = weather.city.name
                setOnClickListener {
                    listener.onItemClick(weather)
                }
            }
        }
    }
}