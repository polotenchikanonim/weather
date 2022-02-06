package local.kas.weather

import android.app.NotificationChannel
import android.app.NotificationManager
import android.util.Log
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class FCMService : FirebaseMessagingService() {

    override fun onNewToken(s: String) {
        super.onNewToken(s)
        Log.d("myLogs", "token $s")
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        val data = remoteMessage.data
        if (data.isNotEmpty()) {
            val title = data["myTitle"]
            val message = data["myMessage"]
            if (!title.isNullOrBlank() && !message.isNullOrEmpty()) {
                pushNotification(title, message)
            }
        }
    }

    private fun pushNotification(title: String, message: String) {

        val channelId = "channel_id"
        val notificationId = 1

        val notificationBuilder = NotificationCompat.Builder(this, channelId).apply {
            setSmallIcon(R.drawable.ic_action_name)
            setContentTitle(title)
            setContentText(message)
            priority = NotificationCompat.PRIORITY_HIGH
        }
        (getSystemService(NOTIFICATION_SERVICE) as NotificationManager).apply {
            createNotificationChannel(
                NotificationChannel(channelId, channelId, NotificationManager.IMPORTANCE_HIGH)
            )
            notify(notificationId, notificationBuilder.build())
        }
    }

}
