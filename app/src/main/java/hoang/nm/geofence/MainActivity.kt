package hoang.nm.geofence

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.maps.model.LatLng
import hoang.nm.geofence.ui.theme.GeofenceTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    private val permissions = arrayOf(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION,
    ).apply {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) plus(Manifest.permission.ACCESS_BACKGROUND_LOCATION)
    }
    private var locationPermissionLauncher: ActivityResultLauncher<Array<String>>? =
        registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { _ ->
            if (hasLocationPermission()) {
                setUpGeofenceWithPermission()
            } else {
                Toast.makeText(this, "Location permission is required", Toast.LENGTH_LONG).show()
            }
        }

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
            GeofenceTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Column(
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        UnregisterButton {
                            lifecycleScope.launch(Dispatchers.IO) {
                                geofenceManager.deregisterGeofence()
                            }
                        }
                    }
                }
            }
        }
        setUpGeofenceWithPermission()
    }

    override fun onDestroy() {
        super.onDestroy()
        locationPermissionLauncher?.unregister()
        locationPermissionLauncher = null
    }

    private fun setUpGeofenceWithPermission() {
        if (hasLocationPermission()) {
            setupGeofence()
        } else {
            locationPermissionLauncher?.launch(permissions)
        }
    }

    private fun setupGeofence() {
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
    }

    private fun hasLocationPermission(): Boolean = permissions.all {
        ContextCompat.checkSelfPermission(this, it) == PackageManager.PERMISSION_GRANTED
    }

}

@Composable
fun UnregisterButton(modifier: Modifier = Modifier, onClick: () -> Unit = {}) {
    Button(
        onClick = onClick,
        modifier = modifier
    ) {
        Text(text = "Unregister receiver")
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    GeofenceTheme {
        UnregisterButton()
    }
}