package com.example.myapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.myapp.ui.screens.SettingsScreen
import com.example.myapp.ui.theme.HandsOn1stTheme

class test_für_settingscreen : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            HandsOn1stTheme {
                SettingsScreen(
                    onManualSync = { println("Sync!") },
                    isPeriodicSyncEnabled = false,
                    onPeriodicSyncToggled = { println("Toggled: $it") }
                )
            }
        }
    }
}
