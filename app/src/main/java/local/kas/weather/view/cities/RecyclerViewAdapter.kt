package local.kas.weather.view.cities

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import local.kas.weather.R
import local.kas.weather.model.City

class RecyclerViewAdapter(val listener: OnItemClickListener) :
    RecyclerView.Adapter<RecyclerViewAdapter.WeatherViewHolder>() {

    private var weatherData: List<City> = listOf()

    fun setWeather(data: List<City>) {
        weatherData = data
        notifyDataSetChanged() // fixme please. Sorry i don't know
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
        fun bind(city: City) {
            itemView.run {
                findViewById<AppCompatTextView>(R.id.weatherItemTextView).text = city.name
                setOnClickListener {
                    listener.onItemClick(city)
                }
            }
        }
    }
}