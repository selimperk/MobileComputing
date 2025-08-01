package com.myapp.myapp.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast

class AirplaneModeReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent?.action == Intent.ACTION_AIRPLANE_MODE_CHANGED) {
            val isAirplaneModeOn = intent.getBooleanExtra("state", false)
            val message = if (isAirplaneModeOn) {
                "Flugmodus aktiviert – Online-Funktionen sind eingeschränkt."
            } else {
                "Flugmodus deaktiviert – Alle Funktionen sind wieder verfügbar."
            }
            Toast.makeText(context, message, Toast.LENGTH_LONG).show()
        }
    }
}
