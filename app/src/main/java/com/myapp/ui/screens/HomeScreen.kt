package com.myapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Person2
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.myapp.model.room.entities.Challenges
import com.myapp.Route.Route
import com.myapp.model.room.entities.Participant

@Composable
fun HomeScreen(
    participantPairs: List<Pair<Participant, Participant>>,
    onChallengeClick: () -> Unit,
    onNavigate: (Route) -> Unit
) {
    Scaffold(
        containerColor = Color(0xFF156082)
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Header
            Button(
                onClick = {},
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF6F1C)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Eigener Punktestand & evtl Rangliste")
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Teilnehmer-Paare anzeigen
            participantPairs.forEach { (p1, p2) ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                        .clickable { onChallengeClick() },
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Orange Box mit Icons + Namen
                    Row(
                        modifier = Modifier
                            .weight(1f)
                            .background(Color(0xFFFF6F1C), shape = RoundedCornerShape(4.dp))
                            .padding(vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        Icon(Icons.Default.Person, contentDescription = "Person")
                        //Text("${p1.firstName} ${p1.lastName}", color = Color.Black)
                        Text(
                            "VS",
                            color = Color.Black,
                            textAlign = TextAlign.Center,
                            fontSize = 16.sp
                        )
                        //Text("${p2.firstName} ${p2.lastName}", color = Color.Black)
                        Icon(Icons.Default.Person2, contentDescription = "Person")
                    }

                    // Rechte Box mit Challenge-Status-Icon (z. B. leer oder Check)
                    Box(
                        modifier = Modifier
                            .padding(start = 8.dp)
                            .size(40.dp)
                            .background(Color(0xFFFF6F1C), shape = RoundedCornerShape(4.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.Check, contentDescription = null, tint = Color.Black)
                    }
                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    val dummyParticipants = listOf(
        Participant(null, "Tim", "Meyer", "123", "tim@example.com"),
        Participant(null, "Lena", "Schmidt", "456", "lena@example.com"),
        Participant(null, "Jonas", "Weber", "789", "jonas@example.com"),
        Participant(null, "Sarah", "Klein", "012", "sarah@example.com")
    )

    val pairs = dummyParticipants.chunked(2)
        .filter { it.size == 2 }
        .map { Pair(it[0], it[1]) }

    HomeScreen(
        participantPairs = pairs,
        onChallengeClick = {},
        onNavigate = {}
    )
}
