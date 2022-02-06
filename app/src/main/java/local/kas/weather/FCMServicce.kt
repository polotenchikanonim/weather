package local.kas.weather

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

@SuppressLint("MissingFirebaseInstanceTokenRefresh")
class FCMService : FirebaseMessagingService() {

    private val channelId = "channelId"
    private val myTitle = "myTitle"
    private val myMessage = "myMessage"

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        val data = remoteMessage.data
        if (data.isNotEmpty()) {
            val title = data[myTitle]
            val message = data[myMessage]
            if (!title.isNullOrBlank() && !message.isNullOrEmpty()) {
                pushNotification(title, message)
            }
        }
    }

    private fun pushNotification(title: String, message: String) {

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
