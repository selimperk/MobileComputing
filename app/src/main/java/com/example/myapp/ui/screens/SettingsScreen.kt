// SettingsScreen.kt
package com.example.myapp.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.myapp.ui.theme.HandsOn1stTheme

@Composable
fun SettingsScreen(
    onManualSync: () -> Unit,
    isPeriodicSyncEnabled: Boolean,
    onPeriodicSyncToggled: (Boolean) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 40.dp, start = 16.dp, end = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally // ⬅️ zentriert Inhalte
    ) {
        Text("Settings", style = MaterialTheme.typography.headlineSmall)
        Spacer(modifier = Modifier.height(20.dp))

        Button(onClick = { onManualSync() }) {
            Text("Jetzt synchronisieren")
        }

        Spacer(modifier = Modifier.height(24.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Automatisch synchronisieren alle 15 Min")
            Spacer(modifier = Modifier.width(8.dp))
            Switch(
                checked = isPeriodicSyncEnabled,
                onCheckedChange = { onPeriodicSyncToggled(it) }
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
fun SettingsScreenPreview() {
    HandsOn1stTheme {
        SettingsScreen(
            onManualSync = {},
            isPeriodicSyncEnabled = false,
            onPeriodicSyncToggled = {}
        )
    }
}
