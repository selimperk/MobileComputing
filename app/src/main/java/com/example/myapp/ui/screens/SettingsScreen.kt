// SettingsScreen.kt
package com.example.myapp.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview
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
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = Icons.Default.Settings,
            contentDescription = "Settings",
            modifier = Modifier.size(48.dp),
            tint = Color.Gray
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Settings",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(32.dp))

        Button(onClick = onManualSync) {
            Text("Jetzt synchronisieren")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Automatisch synchronisieren alle 15 Minuten")
            Spacer(modifier = Modifier.width(8.dp))
            Switch(
                checked = isPeriodicSyncEnabled,
                onCheckedChange = onPeriodicSyncToggled
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