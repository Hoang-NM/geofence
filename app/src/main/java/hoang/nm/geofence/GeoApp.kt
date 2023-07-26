package hoang.nm.geofence

import android.app.Application

class GeoApp : Application() {

    companion object {

        private lateinit var _instance: GeoApp

        val instance: GeoApp
            get() = _instance

    }

    override fun onCreate() {
        super.onCreate()
        _instance = this
    }

}