package com.myapp.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
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
import com.myapp.Route.Route
import kotlinx.coroutines.launch
import com.myapp.viewmodel.SettingsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel,
    modifier: Modifier = Modifier,
    onManualSync: () -> Unit = {},
    isPeriodicSyncEnabled: Boolean = false,
    onPeriodicSyncToggled: (Boolean) -> Unit = {},
    onNavigate: (Route) -> Unit
) {

    val scope = rememberCoroutineScope()

    val language by viewModel.languageFlow.collectAsState(initial = "")
    val userName by viewModel.userNameFlow.collectAsState(initial = "")
    val email by viewModel.emailFlow.collectAsState(initial = "")
    val notificationsEnabled by viewModel.notificationFlow.collectAsState(initial = false)
    val avatarId by viewModel.avatarIdFlow.collectAsState(initial = R.drawable.avatar1)

    var userNameInput by remember { mutableStateOf(userName ?: "") }
    var emailInput by remember { mutableStateOf(email ?: "") }
    var selectedLanguage by remember { mutableStateOf(language ?: "de") }
    var notificationsToggle by remember { mutableStateOf(notificationsEnabled) }
    var isLanguageDropdownOpen by remember { mutableStateOf(false) }
    var selectedAvatar by remember { mutableStateOf(avatarId) }
    var isAvatarDialogOpen by remember { mutableStateOf(false) }

    Scaffold(
        containerColor = Color(0xFF156082)
    ) { innerPadding ->

        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // Header ohne Home-Icon
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFFF6F1C))
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Settings", color = Color.White, fontSize = 20.sp)
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
                    colors = textFieldColors()
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text("E-Mail", fontStyle = FontStyle.Italic, color = Color.Black)
                TextField(
                    value = emailInput,
                    onValueChange = { emailInput = it },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    colors = textFieldColors()
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
                        colors = textFieldColors()
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
                    onCheckedChange = { notificationsToggle = it }
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
                    onCheckedChange = onPeriodicSyncToggled
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Manuelles Synchronisieren
            Button(
                onClick = onManualSync,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(horizontal = 32.dp)
            ) {
                Text("Jetzt synchronisieren")
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Lokales Speichern
            Button(
                onClick = {
                    scope.launch {
                        viewModel.setUserName(userNameInput)
                        viewModel.setEmail(emailInput)
                        viewModel.setLanguage(selectedLanguage)
                        viewModel.setNotificationsEnabled(notificationsToggle)
                        viewModel.setAvatarId(selectedAvatar)
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 32.dp)
            ) {
                Text("Speichern")
            }
            // In die Cloud speichern
            Button(
                onClick = { viewModel.uploadSettingsToCloud() },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 32.dp)
            ) {
                Text("In die Cloud speichern")
            }

// Aus der Cloud laden
            Button(
                onClick = { viewModel.downloadSettingsFromCloud() },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 32.dp)
            ) {
                Text("Aus der Cloud laden")
            }

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

@Composable
private fun textFieldColors() = TextFieldDefaults.colors(
    focusedContainerColor = Color.Transparent,
    unfocusedContainerColor = Color.Transparent,
    focusedIndicatorColor = Color.Black,
    unfocusedIndicatorColor = Color.Black
)

@Preview
@Composable
fun SettingsScreenPreview() {
}
