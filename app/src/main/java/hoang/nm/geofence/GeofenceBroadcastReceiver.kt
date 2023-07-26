package hoang.nm.geofence

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofenceStatusCodes
import com.google.android.gms.location.GeofencingEvent

class GeofenceBroadcastReceiver : BroadcastReceiver() {

    private val TAG = "GeofenceBroadcastReceiver"

    override fun onReceive(context: Context, intent: Intent) {
        val geofencingEvent = GeofencingEvent.fromIntent(intent)
        if (geofencingEvent.hasError()) {
            val errorMessage = GeofenceStatusCodes.getStatusCodeString(geofencingEvent.errorCode)
            Log.e(TAG, errorMessage)
            return
        }
        val geofenceTransition = geofencingEvent.geofenceTransition
        val triggeringGeofences = geofencingEvent.triggeringGeofences
        val geofenceDetails = getGeofenceDetails(geofenceTransition, triggeringGeofences)
        val notificationManager = ContextCompat.getSystemService(
            context, NotificationManager::class.java
        ) as NotificationManager

        notificationManager.sendGeofenceTransitionNotification(context, geofenceDetails)
    }
}

private fun getGeofenceDetails(
    geofenceTransition: Int,
    triggeringGeofences: List<Geofence>
): String {
    val instruction = when (geofenceTransition) {
        Geofence.GEOFENCE_TRANSITION_ENTER -> "You have entered"
        Geofence.GEOFENCE_TRANSITION_EXIT -> "You have exited"
        Geofence.GEOFENCE_TRANSITION_DWELL -> "You have dwelled"
        else -> "Unknown transition in"
    }
    return instruction + " ${triggeringGeofences.joinToString { it.requestId }}"
}

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