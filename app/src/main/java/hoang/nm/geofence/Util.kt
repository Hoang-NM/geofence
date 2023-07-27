package hoang.nm.geofence

import android.content.BroadcastReceiver
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings


const val CUSTOM_REQUEST_CODE_GEOFENCE = 1001

fun Context.openAppSettings(): Boolean {
    return try {
        val settingsIntent = Intent()
        settingsIntent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        settingsIntent.addCategory(Intent.CATEGORY_DEFAULT)
        settingsIntent.setData(Uri.parse("package:$packageName"))
        settingsIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        settingsIntent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
        settingsIntent.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS)
        startActivity(settingsIntent)
        true
    } catch (ex: Exception) {
        false
    }
}

fun Context.openLocationSettings(): Boolean {
    return try {
        val settingsIntent = Intent()
        settingsIntent.setAction(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
        settingsIntent.addCategory(Intent.CATEGORY_DEFAULT)
        settingsIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        settingsIntent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
        settingsIntent.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS)
        startActivity(settingsIntent)
        true
    } catch (ex: Exception) {
        false
    }
}

fun Context.unregisterReceiverFromManifest(clazz: Class<out BroadcastReceiver?>) {
    val component = ComponentName(this, clazz)
    val status = packageManager.getComponentEnabledSetting(component)
    if (status == PackageManager.COMPONENT_ENABLED_STATE_ENABLED) {
        this.packageManager.setComponentEnabledSetting(
            component,
            PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
            PackageManager.DONT_KILL_APP
        )
    }
}
