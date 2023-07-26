package hoang.nm.geofence

import android.Manifest
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.location.Location
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.maps.model.LatLng
import hoang.nm.geofence.ui.theme.GeofencingtestTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    private val permissions = arrayOf(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION,
    )
    private var locationPermissionLauncher: ActivityResultLauncher<Array<String>>? = null

    private var geofenceList = hashMapOf(
        "Hanoi, VTI Building" to LatLng(21.015023300463255, 105.78189508056043),
        "Hanoi, HL Building" to LatLng(21.0322625234294, 105.7827793654397),
        "Hanoi, Handico Tower" to LatLng(21.01748662520395, 105.78207726907681),
    )
    private val geofenceManager by lazy {
        GeofenceManager(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            GeofencingtestTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Greeting("Android")
                }
            }
        }
        locationPermissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { _ ->
            if (hasLocationPermission()) {
                setUpGeofenceWithPermission()
            } else {
                Toast.makeText(this, "no permission", Toast.LENGTH_SHORT).show()
            }
        }
        setUpGeofenceWithPermission()
    }

    override fun onDestroy() {
        super.onDestroy()
        locationPermissionLauncher?.unregister()
        locationPermissionLauncher = null
        lifecycleScope.launch(Dispatchers.IO) {
            geofenceManager.deregisterGeofence()
        }
    }

    private fun setUpGeofenceWithPermission() {
        if (hasLocationPermission()) {
            for (geofence in geofenceList) {
                geofenceManager.addGeofence(
                    geofence.key,
                    location = Location("").apply {
                        latitude = geofence.value.latitude
                        longitude = geofence.value.longitude
                    },
                )
            }
            geofenceManager.registerGeofence()
        } else {
            locationPermissionLauncher?.launch(permissions)
        }
    }

    private fun hasLocationPermission(): Boolean = permissions.all {
        ContextCompat.checkSelfPermission(this, it) == PackageManager.PERMISSION_GRANTED
    }

}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    GeofencingtestTheme {
        Greeting("Android")
    }
}