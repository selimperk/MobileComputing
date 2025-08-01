package com.myapp.datastore.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.myapp.datastore.model.database.entities.Participant
import com.myapp.datastore.viewmodels.ParticipantViewModel

@Composable
fun ParticipantScreen(
    modifier: Modifier = Modifier,
    viewModel: ParticipantViewModel = viewModel()
) {
    val participants by viewModel.allParticipants.collectAsStateWithLifecycle(emptyList())

    ParticipantScreen(
        modifier = modifier,
        participants = participants,
        onDelete = { viewModel.deleteParticipant(it) },
        onSave = { first, last, phone, email, birthdate ->
            viewModel.saveParticipant(first, last, phone, email, birthdate)
        }
    )
}

@Composable
fun ParticipantScreen(
    modifier: Modifier = Modifier,
    participants: List<Participant>,
    onDelete: (Participant) -> Unit,
    onSave: (String, String, String, String, String?) -> Unit
) {
    Column(modifier = modifier.padding(16.dp)) {
        AddParticipantRow(onSave)
        AllParticipants(participants, onDelete)
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
        OutlinedTextField(value = firstName, onValueChange = { firstName = it }, label = { Text("Vorname") })
        OutlinedTextField(value = lastName, onValueChange = { lastName = it }, label = { Text("Nachname") })
        OutlinedTextField(value = phone, onValueChange = { phone = it }, label = { Text("Telefon") })
        OutlinedTextField(value = email, onValueChange = { email = it }, label = { Text("E-Mail") })
        OutlinedTextField(value = birthdate, onValueChange = { birthdate = it }, label = { Text("Geburtsdatum") })

        Button(onClick = {
            onSave(firstName, lastName, phone, email, birthdate.takeIf { it.isNotBlank() })
            firstName = ""
            lastName = ""
            phone = ""
            email = ""
            birthdate = ""
        }) {
            Text("Teilnehmer speichern")
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
    Row(modifier = Modifier.fillMaxWidth().padding(8.dp)) {
        Column(modifier = Modifier.weight(1f)) {
            Text(text = "${participant.firstName} ${participant.lastName}", fontSize = 20.sp)
            Text(text = participant.email)
            Text(text = participant.phone)
            participant.birthdate?.let {
                Text(text = "Geboren: $it")
            }
        }
        IconButton(onClick = { onDelete(participant) }) {
            Icon(imageVector = Icons.Default.Delete, contentDescription = "Delete participant")
        }
    }
}

@Composable
@Preview
fun ParticipantScreenPreview() {
    ParticipantScreen(
        participants = listOf(
            Participant(1, "Max", "Mustermann", "12345678", "max@example.com", "1990-01-01"),
            Participant(2, "Erika", "Musterfrau", "87654321", "erika@example.com", null)
        ),
        onDelete = {},
        onSave = { _, _, _, _, _ -> }
    )
}

