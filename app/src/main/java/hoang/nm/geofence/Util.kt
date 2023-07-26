package hoang.nm.geofence

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings


const val CUSTOM_INTENT_GEOFENCE = "GEOFENCE-TRANSITION-INTENT-ACTION"
const val CUSTOM_REQUEST_CODE_GEOFENCE = 1001

object Util {
    fun openAppSettings(context: Context): Boolean {
        return try {
            val settingsIntent = Intent()
            settingsIntent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
            settingsIntent.addCategory(Intent.CATEGORY_DEFAULT)
            settingsIntent.setData(Uri.parse("package:" + context.packageName))
            settingsIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            settingsIntent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
            settingsIntent.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS)
            context.startActivity(settingsIntent)
            true
        } catch (ex: Exception) {
            false
        }
    }

    fun openLocationSettings(context: Context): Boolean {
        return try {
            val settingsIntent = Intent()
            settingsIntent.setAction(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
            settingsIntent.addCategory(Intent.CATEGORY_DEFAULT)
            settingsIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            settingsIntent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
            settingsIntent.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS)
            context.startActivity(settingsIntent)
            true
        } catch (ex: Exception) {
            false
        }
    }
}