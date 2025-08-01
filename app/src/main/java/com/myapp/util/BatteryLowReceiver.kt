package com.myapp.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast

class BatteryLowReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == Intent.ACTION_BATTERY_LOW) {
            Toast.makeText(
                context,
                "Akkustand niedrig! Challenge-Synchronisierung pausiert ⚠️",
                Toast.LENGTH_LONG
            ).show()
        }
    }
}
