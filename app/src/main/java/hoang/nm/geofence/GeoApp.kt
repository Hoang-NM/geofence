package hoang.nm.geofence

import android.app.Application
import android.content.IntentFilter
import android.os.Build

class GeoApp : Application() {

    companion object {

        private lateinit var _instance: GeoApp

        val instance: GeoApp
            get() = _instance

    }

    private val receiver by lazy {
        GeofenceBroadcastReceiver()
    }

    override fun onCreate() {
        super.onCreate()
        _instance = this
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            registerReceiver(receiver, IntentFilter(CUSTOM_INTENT_GEOFENCE), RECEIVER_NOT_EXPORTED)
        } else {
            registerReceiver(receiver, IntentFilter(CUSTOM_INTENT_GEOFENCE))
        }
    }

}