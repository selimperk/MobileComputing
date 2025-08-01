package com.myapp.myapp

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import com.myapp.myapp.util.NotificationScheduler // 🔔 NotificationScheduler importieren
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

class MainActivity : ComponentActivity() {

    // 🔔 Notification: Berechtigungs-Callback registrieren
    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            if (isGranted) {
                NotificationScheduler.scheduleDailyNotification(this)
            }
        }
    // bis hierhin NOTIFICATIONSERVICE


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Set-Content Funktion nicht vergessen!!!

        // 🔔 Notification: Berechtigung abfragen & Worker starten
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS)
                != PackageManager.PERMISSION_GRANTED
            ) {
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            } else {
                NotificationScheduler.scheduleDailyNotification(this)
            }
        } else {
            NotificationScheduler.scheduleDailyNotification(this)
        }

        //BIS HIERHIN NOTIFICATIONSERVICE
    }
}

// 🧪 UI bleibt unberührt
@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}


