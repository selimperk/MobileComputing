package com.myapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.RadioButtonUnchecked
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
import com.myapp.model.room.entities.Challenges
import com.myapp.viewmodel.ChallengesViewModel
import androidx.compose.ui.tooling.preview.Preview


@Composable
fun ChallengeScreen(
    modifier: Modifier = Modifier,
    viewModel: ChallengesViewModel = viewModel()
) {
    val challenges by viewModel.allChallenges.collectAsStateWithLifecycle(emptyList())

    ChallengeScreenContent(
        modifier = modifier,
        challenges = challenges,
        onDelete = { viewModel.deleteChallenge(it) },
        onSave = { title, desc ->
            viewModel.saveChallenge(title = title, description = desc)
        },
        onToggleDone = { challenge ->
            viewModel.updateChallenge(challenge.copy(isDone = !challenge.isDone))
        }
    )
}

@Composable
fun ChallengeScreenContent(
    modifier: Modifier = Modifier,
    challenges: List<Challenges>,
    onDelete: (Challenges) -> Unit,
    onSave: (String, String?) -> Unit,
    onToggleDone: (Challenges) -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFF156082))
    ) {
        // Header mit Home-Icon und "Challenges"
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFFF6F1C))
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(Icons.Default.Home, contentDescription = "Home", tint = Color.Black)
            Spacer(modifier = Modifier.weight(1f))
            Text("Challenges", color = Color.White, fontSize = 20.sp)
        }

        Spacer(modifier = Modifier.height(24.dp))

        Column(modifier = Modifier.padding(horizontal = 32.dp)) {
            AddChallengeRow(onSave)
        }

        Spacer(modifier = Modifier.height(16.dp))

        Column(modifier = Modifier.padding(horizontal = 32.dp)) {
            AllChallenges(challenges, onToggleDone, onDelete)
        }
    }
}

@Composable
fun AddChallengeRow(onSave: (String, String?) -> Unit) {
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }

    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text("Titel", fontStyle = FontStyle.Italic, color = Color.Black)
        TextField(
            value = title,
            onValueChange = { title = it },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                focusedIndicatorColor = Color.Black,
                unfocusedIndicatorColor = Color.Black
            )
        )
        Text("Beschreibung", fontStyle = FontStyle.Italic, color = Color.Black)
        TextField(
            value = description,
            onValueChange = { description = it },
            singleLine = false,
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
                if (title.isNotBlank()) {
                    onSave(title, description.takeIf { it.isNotBlank() })
                    title = ""
                    description = ""
                }
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF8E63CE))
        ) {
            Text("Challenge speichern", color = Color.White)
        }
    }
}

@Composable
fun AllChallenges(
    challenges: List<Challenges>,
    onToggleDone: (Challenges) -> Unit,
    onDelete: (Challenges) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(challenges) { challenge ->
            ChallengeRow(challenge, onToggleDone, onDelete)
        }
    }
}

@Composable
fun ChallengeRow(
    challenge: Challenges,
    onToggleDone: (Challenges) -> Unit,
    onDelete: (Challenges) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = challenge.title,
                fontSize = 20.sp,
                color = Color.Black
            )
            challenge.description?.let {
                Text(
                    text = it,
                    color = Color.DarkGray,
                    fontStyle = FontStyle.Italic,
                    fontSize = 14.sp
                )
            }
        }
        IconButton(onClick = { onToggleDone(challenge) }) {
            Icon(
                imageVector = if (challenge.isDone) Icons.Default.CheckCircle else Icons.Default.RadioButtonUnchecked,
                contentDescription = "Toggle Done",
                tint = if (challenge.isDone) Color(0xFF13B566) else Color.Gray
            )
        }
        IconButton(onClick = { onDelete(challenge) }) {
            Icon(
                imageVector = Icons.Default.Delete,
                contentDescription = "Delete challenge",
                tint = Color.Black
            )
        }
    }
}

// Dummy Daten für Preview
@Preview(showBackground = true)
@Composable
fun ChallengeScreenPreview() {
    ChallengeScreenContent(
        challenges = listOf(
            Challenges(
                id = 1,
                title = "Dornseifer-Challenge",
                //description = "Mache ein Sellfie vor dem Rewe-Dornseifer in Gummersbach.",
                isDone = false,
                createdAt = System.currentTimeMillis(),
                dueDate = null
            ),
            Challenges(
                id = 2,
                title = "Voice-Power",
                //description = "Schreie so laut ins Microphone wie geht.",
                isDone = true,
                createdAt = System.currentTimeMillis(),
                dueDate = null
            ),
            Challenges(
                id = 3,
                title = "Run, Forrest, Run!",
                //description = "Mache 1000 Schritte am Stück",
                isDone = false,
                createdAt = System.currentTimeMillis(),
                dueDate = null
            ),
        ),
        onDelete = {},
        onSave = { _, _ -> },
        onToggleDone = {}
    )
}