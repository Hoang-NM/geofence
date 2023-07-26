package hoang.nm.geofence

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat

private const val NOTIFICATION_ID = 999
private const val CHANNEL_ID = "GeofenceChannel"

fun Context.createNotificationChannel() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val notificationChannel =
            NotificationChannel(CHANNEL_ID, "Channel Test", NotificationManager.IMPORTANCE_DEFAULT)
        val notificationManager = getSystemService(NotificationManager::class.java)
        notificationManager.createNotificationChannel(notificationChannel)
    }
}

fun NotificationManager.sendGeofenceTransitionNotification(context: Context, details: String) {

    val contentIntent = Intent(CUSTOM_INTENT_GEOFENCE)
    val contentPendingIntent = PendingIntent.getActivity(
        context,
        CUSTOM_REQUEST_CODE_GEOFENCE,
        contentIntent,
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.S) {
            PendingIntent.FLAG_UPDATE_CURRENT
        } else {
            PendingIntent.FLAG_MUTABLE
        }
    )

    val builder = NotificationCompat.Builder(context, CHANNEL_ID)
        .setContentTitle(context.getString(R.string.app_name))
        .setContentText(details)
        .setStyle(NotificationCompat.BigTextStyle().bigText(details))
        .setSmallIcon(R.drawable.ic_launcher_foreground)
        .setPriority(NotificationCompat.PRIORITY_HIGH)
        .setContentIntent(contentPendingIntent)
        .build()

    context.createNotificationChannel()
    notify(NOTIFICATION_ID, builder)
}