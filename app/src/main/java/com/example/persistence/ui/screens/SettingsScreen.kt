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


@Composable
fun SettingsScreen(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val dataStore = remember { DataStore(context) }
    val scope = rememberCoroutineScope()

    val language by dataStore.languageFlow.collectAsState(initial = "")
    val userName by dataStore.userNameFlow.collectAsState(initial = "")

    var langInput by remember { mutableStateOf(language ?: "") }
    var nameInput by remember { mutableStateOf(userName ?: "") }

    Column(modifier = modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Text("Einstellungen", fontSize = 24.sp)

        OutlinedTextField(
            value = langInput,
            onValueChange = { langInput = it },
            label = { Text("Sprache (z. B. de, en)") }
        )
        Button(onClick = {
            scope.launch { dataStore.setLanguage(langInput) }
        }) {
            Text("Sprache speichern")
        }

        OutlinedTextField(
            value = nameInput,
            onValueChange = { nameInput = it },
            label = { Text("Benutzername") }
        )
        Button(onClick = {
            scope.launch { dataStore.setUserName(nameInput) }
        }) {
            Text("Benutzername speichern")
        }
    }
}

@Preview
@Composable
fun SettingsScreenPreview() {
    SettingsScreen()
}