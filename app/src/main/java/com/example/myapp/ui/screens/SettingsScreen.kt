package com.example.myapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapp.ui.theme.HandsOn1stTheme
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.foundation.Image
import androidx.compose.foundation.lazy.grid.*
import com.example.handson1st.R

@Composable
fun SettingsScreen(
    onManualSync: () -> Unit,
    isPeriodicSyncEnabled: Boolean,
    onPeriodicSyncToggled: (Boolean) -> Unit
) {
    var selectedAvatar by remember { mutableStateOf(R.drawable.avatar1) }
    var isAvatarDialogOpen by remember { mutableStateOf(false) }
    var username by remember { mutableStateOf(TextFieldValue("Maxi-Mustimann")) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF156082)),
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
        }

        Spacer(modifier = Modifier.height(40.dp))

        // Avatar Kreis mit aktuellem Avatar
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
                contentDescription = "Ausgewählter Avatar",
                modifier = Modifier.size(90.dp)
            )
        }

        Spacer(modifier = Modifier.height(30.dp))

        // Username
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 32.dp),
            horizontalAlignment = Alignment.Start
        ) {
            Text("Username", fontStyle = FontStyle.Italic, color = Color.Black)
            TextField(
                value = username,
                onValueChange = { username = it },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    focusedIndicatorColor = Color.Black,
                    unfocusedIndicatorColor = Color.Black
                )
            )
        }

        Spacer(modifier = Modifier.height(40.dp))

        // Sync Sektion
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Button(onClick = onManualSync) {
                Text("Jetzt synchronisieren")
            }

            Spacer(modifier = Modifier.height(20.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("Automatisch synchronisieren alle 15 Min", color = Color.White)
                Spacer(modifier = Modifier.width(8.dp))
                Switch(
                    checked = isPeriodicSyncEnabled,
                    onCheckedChange = onPeriodicSyncToggled
                )
            }
        }
    }

    // Avatar-Auswahl-Dialog
    if (isAvatarDialogOpen) {
        AlertDialog(
            onDismissRequest = { isAvatarDialogOpen = false },
            confirmButton = {},
            title = { Text("Wähle deinen Avatar") },
            text = {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    val avatarIds = listOf(
                        R.drawable.avatar1, R.drawable.avatar2, R.drawable.avatar3,
                        R.drawable.avatar4, R.drawable.avatar5, R.drawable.avatar6
                    )
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(3),
                        modifier = Modifier.height(220.dp)
                    ) {
                        items(avatarIds.size) { index ->
                            val avatarId = avatarIds[index]
                            Image(
                                painter = painterResource(id = avatarId),
                                contentDescription = "Avatar $index",
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
            }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun SettingsScreenPreview() {
    HandsOn1stTheme {
        SettingsScreen(
            onManualSync = { println("Sync!") },
            isPeriodicSyncEnabled = true,
            onPeriodicSyncToggled = { println("Toggled: $it") }
        )
    }
}
