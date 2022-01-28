package local.kas.weather.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class WeatherHistory(
    var cityName: String,
    val temperature: Int,
    val feelsLike: Int ,
    val icon: String = "skc_n"
) : Parcelable
