package hoang.nm.geofence

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
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