package local.kas.weather

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.util.Log


class MyBroadcastReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        if (isOnline(context!!)) {
            Log.d("myLogs", "connect")
        } else {
            Log.d("myLogs", "disconnect")
        }
    }

    private fun isOnline(context: Context): Boolean {
        return try {
            val connectivityManager = context
                .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val network = connectivityManager.activeNetwork
            val networkCapabilities = connectivityManager.getNetworkCapabilities(network)
            val wifi = networkCapabilities!!.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)
            val cellular = networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)
            wifi || cellular
        } catch (e: NullPointerException) {
            e.printStackTrace()
            false
        }
    }
}