package local.kas.weather.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize


@Parcelize
data class WeatherDTO(
//    val fact: Fact
    val temp: Int,
    val feelsLike: Int
) : Parcelable

//data class Fact(
//    val temp: Int,
//    val feelsLike: Int
//)