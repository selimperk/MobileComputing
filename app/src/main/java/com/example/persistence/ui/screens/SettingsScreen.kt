package com.example.persistence.ui.screens

import android.content.Context
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.persistence.model.DataStore
import kotlinx.coroutines.launch
import androidx.compose.ui.Alignment


@Composable
fun SettingsScreen(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val dataStore = remember { DataStore(context) }
    val scope = rememberCoroutineScope()

    val language by dataStore.languageFlow.collectAsState(initial = "")
    val userName by dataStore.userNameFlow.collectAsState(initial = "")
    val email by dataStore.emailFlow.collectAsState(initial = "")
    val notificationsEnabled by dataStore.notificationFlow.collectAsState(initial = false)

    var userNameInput by remember { mutableStateOf(userName ?: "") }
    var emailInput by remember { mutableStateOf(email ?: "") }
    var languageInput by remember { mutableStateOf(language ?: "") }
    var notificationsToggle by remember { mutableStateOf(notificationsEnabled) }

    Column(
        modifier = modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("Einstellungen", fontSize = 24.sp)

        // Profilbild-Platzhalter
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp),
            contentAlignment = Alignment.Center
        ) {
            Text("🧑 Profilbild", fontSize = 32.sp)
        }

        // Benutzername
        OutlinedTextField(
            value = userNameInput,
            onValueChange = { userNameInput = it },
            label = { Text("Benutzername") },
            modifier = Modifier.fillMaxWidth()
        )

        // E-Mail
        OutlinedTextField(
            value = emailInput,
            onValueChange = { emailInput = it },
            label = { Text("E-Mail") },
            modifier = Modifier.fillMaxWidth()
        )

        // Sprache
        OutlinedTextField(
            value = languageInput,
            onValueChange = { languageInput = it },
            label = { Text("Sprache (z. B. de, en)") },
            modifier = Modifier.fillMaxWidth()
        )


        // Push Notification Switch
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Push-Benachrichtigungen", fontSize = 16.sp)
            Spacer(modifier = Modifier.weight(1f))
            Switch(
                checked = notificationsToggle,
                onCheckedChange = { notificationsToggle = it }
            )
        }

        // Speicher-Button
        Button(
            onClick = {
                scope.launch {
                    dataStore.setUserName(userNameInput)
                    dataStore.setEmail(emailInput)
                    dataStore.setLanguage(languageInput)
                    dataStore.setNotificationsEnabled(notificationsToggle)
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Speichern")
        }
    }
}

@Preview
@Composable
fun SettingsScreenPreview() {
    SettingsScreen()
}