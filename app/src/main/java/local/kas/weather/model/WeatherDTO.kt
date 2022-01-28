package local.kas.weather.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize


@Parcelize
data class WeatherDTO(
    val temp: Int,
    val feelsLike: Int,
    val icon: String
) : Parcelable