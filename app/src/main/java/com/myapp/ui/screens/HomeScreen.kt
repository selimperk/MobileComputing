package com.myapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Person2
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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

            // Scrollbare Liste der Teilnehmer-Paare
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(items = participantPairs) { pair ->
                    val (p1, p2) = pair

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onChallengeClick() },
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Orange Box mit Icons und Trennern
                        Row(
                            modifier = Modifier
                                .weight(1f)
                                .background(Color(0xFFFF6F1C), shape = RoundedCornerShape(4.dp))
                                .padding(vertical = 8.dp, horizontal = 12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(Icons.Default.Person, contentDescription = "Person")

                            Spacer(Modifier.width(12.dp))
                            DividerVertical()
                            Spacer(Modifier.width(12.dp))

                            Text(
                                "VS",
                                color = Color.Black,
                                textAlign = TextAlign.Center,
                                fontSize = 16.sp,
                                modifier = Modifier.weight(1f)
                            )

                            Spacer(Modifier.width(12.dp))
                            DividerVertical()
                            Spacer(Modifier.width(12.dp))

                            Icon(Icons.Default.Person2, contentDescription = "Person")
                        }

                        // Rechte Box mit Status-Icon
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

                // Kleiner Spacer am Ende
                item { Spacer(modifier = Modifier.height(8.dp)) }
            }
        }
    }
}

@Composable
private fun DividerVertical() {
    Box(
        modifier = Modifier
            .width(1.dp)
            .height(20.dp)
            .background(Color.Black.copy(alpha = 0.6f))
    )
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
        .map { it[0] to it[1] }

    HomeScreen(
        participantPairs = pairs,
        onChallengeClick = {},
        onNavigate = {}
    )
}
