package local.kas.weather.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class City(val name: String, val latitude: Double, val longitude: Double) : Parcelable

fun getDefaultCity() = City("Moscow", 55.7522, 37.6156)