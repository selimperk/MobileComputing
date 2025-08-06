package com.myapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.myapp.model.room.entities.Participant
import com.myapp.viewmodel.ParticipantViewModel
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun ParticipantScreen(
    modifier: Modifier = Modifier,
    viewModel: ParticipantViewModel = viewModel()
) {
    val participants by viewModel.allParticipants.collectAsStateWithLifecycle(emptyList())

    ParticipantScreenContent(
        modifier = modifier,
        participants = participants,
        onDelete = { viewModel.deleteParticipant(it) },
        onSave = { first, last, phone, email, birthdate ->
            viewModel.saveParticipant(first, last, phone, email, birthdate)
        }
    )
}

@Composable
fun ParticipantScreenContent(
    modifier: Modifier = Modifier,
    participants: List<Participant>,
    onDelete: (Participant) -> Unit,
    onSave: (String, String, String, String, String?) -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFF156082)) // Hintergrundfarbe dunkelblau
    ) {
        // Header (Orange Balken mit Home-Icon)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFFF6F1C)) // Orange Balken
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(Icons.Default.Home, contentDescription = "Home", tint = Color.Black)
            Spacer(modifier = Modifier.weight(1f))
            Text("Participants", color = Color.White, fontSize = 20.sp)
        }

        Spacer(modifier = Modifier.height(24.dp))

        Column(modifier = Modifier.padding(horizontal = 32.dp)) {
            AddParticipantRow(onSave)
        }

        Spacer(modifier = Modifier.height(16.dp))

        Column(modifier = Modifier.padding(horizontal = 32.dp)) {
            AllParticipants(participants, onDelete)
        }
    }
}

@Composable
fun AddParticipantRow(onSave: (String, String, String, String, String?) -> Unit) {
    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var birthdate by remember { mutableStateOf("") }

    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        // Alle Labels italic & schwarz, wie in SettingsScreen
        Text("Vorname", fontStyle = FontStyle.Italic, color = Color.Black)
        TextField(
            value = firstName,
            onValueChange = { firstName = it },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                focusedIndicatorColor = Color.Black,
                unfocusedIndicatorColor = Color.Black
            )
        )
        Text("Nachname", fontStyle = FontStyle.Italic, color = Color.Black)
        TextField(
            value = lastName,
            onValueChange = { lastName = it },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                focusedIndicatorColor = Color.Black,
                unfocusedIndicatorColor = Color.Black
            )
        )
        Text("Telefon", fontStyle = FontStyle.Italic, color = Color.Black)
        TextField(
            value = phone,
            onValueChange = { phone = it },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                focusedIndicatorColor = Color.Black,
                unfocusedIndicatorColor = Color.Black
            )
        )
        Text("E-Mail", fontStyle = FontStyle.Italic, color = Color.Black)
        TextField(
            value = email,
            onValueChange = { email = it },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                focusedIndicatorColor = Color.Black,
                unfocusedIndicatorColor = Color.Black
            )
        )
        Text("Geburtsdatum", fontStyle = FontStyle.Italic, color = Color.Black)
        TextField(
            value = birthdate,
            onValueChange = { birthdate = it },
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

        Button(
            onClick = {
                onSave(firstName, lastName, phone, email, birthdate.takeIf { it.isNotBlank() })
                firstName = ""
                lastName = ""
                phone = ""
                email = ""
                birthdate = ""
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF8E63CE))
        ) {
            Text("Teilnehmer speichern", color = Color.White)
        }
    }
}

@Composable
fun AllParticipants(
    participants: List<Participant>,
    onDelete: (Participant) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(participants) { participant ->
            ParticipantRow(participant, onDelete)
        }
    }
}

@Composable
fun ParticipantRow(
    participant: Participant,
    onDelete: (Participant) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = "${participant.firstName} ${participant.lastName}",
                fontSize = 20.sp,
                color = Color.Black
            )
            Text(text = participant.email, color = Color.Black)
            Text(text = participant.phone, color = Color.Black)
            participant.birthdate?.let {
                Text(text = "Geboren: $it", color = Color.Black)
            }
        }
        IconButton(onClick = { onDelete(participant) }) {
            Icon(
                imageVector = Icons.Default.Delete,
                contentDescription = "Delete participant",
                tint = Color.Black
            )
        }
    }
}

@Preview
@Composable
fun ParticipantScreenPreview() {
    ParticipantScreenContent(
        participants = listOf(
            Participant(1, "Max", "Mustermann", "12345678", "max@example.com", "1990-01-01"),
            Participant(2, "Erika", "Musterfrau", "87654321", "erika@example.com", null)
        ),
        onDelete = {},
        onSave = { _, _, _, _, _ -> }
    )
}
