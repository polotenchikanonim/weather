package local.kas.weather

import android.app.IntentService
import android.content.Intent
import android.util.Log

const val MAIN_SERVICE_KEY_EXTRAS = "MAIN_SERVICE_KEY_EXTRAS"
const val TAG = "myLogs"

class MyService(name: String = "") : IntentService(name) {

    private fun createLogMessage(message: String) {
        Log.d(TAG, message)
    }

    override fun onHandleIntent(intent: Intent?) {
        createLogMessage("onHandleIntent ${intent?.getStringExtra(MAIN_SERVICE_KEY_EXTRAS)}")
    }

    override fun onCreate() {
        super.onCreate()
        createLogMessage("onCreate")
    }

}