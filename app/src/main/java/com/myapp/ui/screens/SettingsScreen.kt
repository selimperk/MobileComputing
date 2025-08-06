package com.myapp.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.myapp.model.DataStore
import com.example.handson1st.R
import kotlinx.coroutines.launch

Absolut! Für den SettingsScreen ist die Integration der Netzwerk-Sicherheit etwas anders, da dieser Screen bereits Callback-Parameter für die Synchronisation (onManualSync, onPeriodicSyncToggled) besitzt. Wir werden diese nutzen und das ViewModel dazwischenschalten.

Hier ist der komplette, aktualisierte Code für deinen SettingsScreen.kt:
Kotlin

package com.myapp.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Refresh // <<-- NEU: Import für Refresh Icon
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle // <<-- NEU: Für StateFlow Beobachtung
import androidx.lifecycle.viewmodel.compose.viewModel // <<-- NEU: Für ViewModel Instanzierung
import com.myapp.model.DataStore // Dein DataStore
import com.example.handson1st.R
import kotlinx.coroutines.launch
import com.myapp.viewmodel.SettingsViewModel // <<-- NEU: Importiere dein SettingsViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    modifier: Modifier = Modifier,
    // <<-- GEÄNDERT: ViewModel wird nun hier übergeben
    settingsViewModel: SettingsViewModel = viewModel()
) {
    val context = LocalContext.current
    // Der DataStore sollte idealerweise auch im ViewModel verwaltet werden,
    // aber für diese Iteration lassen wir ihn hier, wenn er nur für diesen Screen ist.
    val dataStore = remember { DataStore(context) }
    val scope = rememberCoroutineScope() // Für DataStore Operationen

    // <<-- NEÄNDERT/NEU: Beobachten der States aus dem SettingsViewModel
    val language by settingsViewModel.languageFlow.collectAsStateWithLifecycle("")
    val userName by settingsViewModel.userNameFlow.collectAsStateWithLifecycle("")
    val email by settingsViewModel.emailFlow.collectAsStateWithLifecycle("")
    val notificationsEnabled by settingsViewModel.notificationFlow.collectAsStateWithLifecycle(false)
    val avatarId by settingsViewModel.avatarIdFlow.collectAsStateWithLifecycle(R.drawable.avatar1)

    // <<-- NEU: Beobachten des Synchronisationsstatus und der Auto-Sync-Einstellung vom ViewModel
    val isSyncing by settingsViewModel.isSyncing.collectAsStateWithLifecycle(false)
    val isPeriodicSyncEnabled by settingsViewModel.isPeriodicSyncEnabled.collectAsStateWithLifecycle(false)


    // Hier bleiben die lokalen States für die Eingabefelder bestehen,
    // da sie direkt vom Benutzer bearbeitet werden und nur beim Speichern ins DataStore geschrieben werden.
    var userNameInput by remember(userName) { mutableStateOf(userName) }
    var emailInput by remember(email) { mutableStateOf(email) }
    var selectedLanguage by remember(language) { mutableStateOf(language) }
    var notificationsToggle by remember(notificationsEnabled) { mutableStateOf(notificationsEnabled) }
    var isLanguageDropdownOpen by remember { mutableStateOf(false) }
    var selectedAvatar by remember(avatarId) { mutableStateOf(avatarId) }
    var isAvatarDialogOpen by remember { mutableStateOf(false) }


    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFF156082))
    ) {
        // Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFFF6F1C))
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(Icons.Default.Home, contentDescription = "Home", tint = Color.Black)
            Spacer(modifier = Modifier.weight(1f))
            Text("Settings", color = Color.White, fontSize = 20.sp)

            // <<-- NEU: Button für manuelle Synchronisation und Indikator im Header
            // Nur anzeigen, wenn sync-Funktion im ViewModel vorhanden ist
            if (isSyncing) {
                CircularProgressIndicator(
                    modifier = Modifier.size(24.dp).padding(start = 8.dp),
                    color = Color.White,
                    strokeWidth = 2.dp
                )
            } else {
                IconButton(onClick = { settingsViewModel.syncSettingsData() }) { // <<-- HIER: Manuelle Sync aufrufen
                    Icon(Icons.Default.Refresh, contentDescription = "Refresh data", tint = Color.White)
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Avatar
        Box(
            modifier = Modifier
                .size(100.dp)
                .align(Alignment.CenterHorizontally)
                .clip(CircleShape)
                .background(Color.Gray)
                .clickable { isAvatarDialogOpen = true },
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = selectedAvatar),
                contentDescription = "Avatar",
                modifier = Modifier.size(90.dp)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Eingabefelder
        Column(
            modifier = Modifier.padding(horizontal = 32.dp),
            horizontalAlignment = Alignment.Start
        ) {
            Text("Benutzername", fontStyle = FontStyle.Italic, color = Color.Black)
            TextField(
                value = userNameInput,
                onValueChange = { userNameInput = it },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    focusedIndicatorColor = Color.Black,
                    unfocusedIndicatorColor = Color.Black
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text("E-Mail", fontStyle = FontStyle.Italic, color = Color.Black)
            TextField(
                value = emailInput,
                onValueChange = { emailInput = it },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    focusedIndicatorColor = Color.Black,
                    unfocusedIndicatorColor = Color.Black
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text("Sprache", fontStyle = FontStyle.Italic, color = Color.Black)
            ExposedDropdownMenuBox(
                expanded = isLanguageDropdownOpen,
                onExpandedChange = { isLanguageDropdownOpen = !isLanguageDropdownOpen }
            ) {
                TextField(
                    readOnly = true,
                    value = selectedLanguage,
                    onValueChange = {},
                    modifier = Modifier.menuAnchor().fillMaxWidth(),
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = isLanguageDropdownOpen) },
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent
                    )
                )
                ExposedDropdownMenu(
                    expanded = isLanguageDropdownOpen,
                    onDismissRequest = { isLanguageDropdownOpen = false }
                ) {
                    listOf("de", "en").forEach { lang ->
                        DropdownMenuItem(
                            text = { Text(lang) },
                            onClick = {
                                selectedLanguage = lang
                                isLanguageDropdownOpen = false
                            }
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Push Notifications
        Row(
            modifier = Modifier
                .padding(horizontal = 32.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Push Notifications", color = Color.White)
            Spacer(modifier = Modifier.weight(1f))
            Switch(
                checked = notificationsToggle,
                onCheckedChange = {
                    notificationsToggle = it
                    // <<-- Optional: ViewModel über Änderung informieren
                    scope.launch { dataStore.setNotificationsEnabled(it) }
                }
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Auto Sync
        Row(
            modifier = Modifier
                .padding(horizontal = 32.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Auto-Sync alle 15 Min", color = Color.White)
            Spacer(modifier = Modifier.weight(1f))
            Switch(
                checked = isPeriodicSyncEnabled,
                onCheckedChange = {
                    // <<-- HIER: Änderungen der Auto-Sync-Einstellung an ViewModel delegieren
                    settingsViewModel.togglePeriodicSync(it)
                }
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Manuelles Synchronisieren
        Button(
            onClick = { settingsViewModel.syncSettingsData() }, // <<-- HIER: Manuelle Sync an ViewModel delegieren
            enabled = !isSyncing, // <<-- NEU: Button deaktivieren, wenn bereits synchronisiert wird
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(horizontal = 32.dp)
        ) {
            Text(if (isSyncing) "Synchronisiere..." else "Jetzt synchronisieren") // <<-- NEU: Textänderung
            if (isSyncing) { // Optional: Ladeindikator direkt im Button, wenn Button nur Text und Icon hat
                Spacer(Modifier.width(8.dp))
                CircularProgressIndicator(Modifier.size(20.dp), color = Color.White)
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Speichern
        Button(
            onClick = {
                scope.launch {
                    settingsViewModel.setUserName(userNameInput)     // <<-- GEÄNDERT: ViewModel-Methoden nutzen
                    settingsViewModel.setEmail(emailInput)           // <<-- GEÄNDERT: ViewModel-Methoden nutzen
                    settingsViewModel.setLanguage(selectedLanguage)  // <<-- GEÄNDERT: ViewModel-Methoden nutzen
                    // notificationsToggle wurde schon oben gehandhabt
                    settingsViewModel.setAvatarId(selectedAvatar)    // <<-- GEÄNDERT: ViewModel-Methoden nutzen
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 32.dp)
        ) {
            Text("Speichern")
        }
    }

    // Avatar Auswahl Dialog
    if (isAvatarDialogOpen) {
        AlertDialog(
            onDismissRequest = { isAvatarDialogOpen = false },
            confirmButton = {},
            title = { Text("Wähle deinen Avatar") },
            text = {
                val avatarIds = listOf(
                    R.drawable.avatar1, R.drawable.avatar2, R.drawable.avatar3,
                    R.drawable.avatar4, R.drawable.avatar5, R.drawable.avatar6
                )
                LazyVerticalGrid(
                    columns = GridCells.Fixed(3),
                    modifier = Modifier.height(220.dp)
                ) {
                    items(avatarIds) { avatarId ->
                        Image(
                            painter = painterResource(id = avatarId),
                            contentDescription = "Avatar",
                            modifier = Modifier
                                .padding(8.dp)
                                .size(70.dp)
                                .clip(CircleShape)
                                .clickable {
                                    selectedAvatar = avatarId
                                    isAvatarDialogOpen = false
                                }
                        )
                    }
                }
            }
        )
    }
}

@Preview
@Composable
fun SettingsScreenPreview() {
    SettingsScreen(settingsViewModel = viewModel()) // <<-- GEÄNDERT: ViewModel für Preview
}