package com.myapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.myapp.model.Challenge
import com.example.handson1st.R


@Composable
fun HomeScreen(
    // ViewModel über Compose's viewModel() bereitstellen
    homeViewModel: HomeViewModel = viewModel(), // <<-- GEÄNDERT: ViewModel-Parameter hinzugefügt
    challenges: List<Challenge>, // Bleibt bestehen, wenn du Challenges direkt übergibst
    onChallengeClick: (Challenge) -> Unit,
    onNavigateToSettings: () -> Unit,
    onNavigateToParticipants: () -> Unit,
    onNavigateToHome: () -> Unit
) {
    // 1. Synchronisationsstatus vom ViewModel beobachten
    val isSyncing by homeViewModel.isSyncing.collectAsStateWithLifecycle(false) // <<-- NEU

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF156082))
            .padding(horizontal = 16.dp, vertical = 12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Header
        Button(
            onClick = {
                // 2. Synchronisation auslösen, wenn der Button geklickt wird
                homeViewModel.syncHomeData() // <<-- NEU: Aufruf der ViewModel-Methode
            },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF6F1C)),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Eigener Punktestand & evtl Rangliste")
            // 3. Optional: Ladeindikator anzeigen, wenn synchronisiert wird
            if (isSyncing) { // <<-- NEU: Bedingte Anzeige
                Spacer(Modifier.width(8.dp))
                CircularProgressIndicator(
                    modifier = Modifier.size(20.dp),
                    color = Color.White,
                    strokeWidth = 2.dp
                )
            } else {
                Spacer(Modifier.width(8.dp))
                Icon(Icons.Default.Refresh, contentDescription = "Refresh data", tint = Color.White) // <<-- NEU: Refresh-Icon, wenn nicht synchronisiert wird
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Challenges
        challenges.forEach { challenge ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
                    .clickable { onChallengeClick(challenge) },
                verticalAlignment = Alignment.CenterVertically
            ) {
                // ⬅️ Orange Box für Icon – VS – Icon
                Row(
                    modifier = Modifier
                        .weight(1f)
                        .background(Color(0xFFFF6F1C), shape = RoundedCornerShape(4.dp))
                        .padding(vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = null,
                        tint = Color.Black
                    )

                    Text(
                        "VS",
                        color = Color.Black,
                        textAlign = TextAlign.Center,
                        fontSize = 16.sp
                    )

                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = null,
                        tint = Color.Black
                    )
                }

                // ✅ Orange Box für Check/Close
                Box(
                    modifier = Modifier
                        .padding(start = 8.dp)
                        .size(40.dp)
                        .background(Color(0xFFFF6F1C), shape = RoundedCornerShape(4.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = if (challenge.won) Icons.Default.Check else Icons.Default.Close,
                        contentDescription = null,
                        tint = Color.Black
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Bottom Navigation
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFFF6F1C), shape = RoundedCornerShape(4.dp))
                .height(56.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .clickable { onNavigateToHome() },
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Default.Home, contentDescription = null, tint = Color.Black)
            }

            Divider(
                color = Color.Black,
                modifier = Modifier
                    .width(1.dp)
                    .fillMaxHeight()
            )

            Box(
                modifier = Modifier
                    .weight(1f)
                    .clickable { onNavigateToParticipants() },
                contentAlignment = Alignment.Center
            ) {
                Icon(painter = painterResource(R.drawable.ic_add), contentDescription = null, tint = Color.Black)
            }

            Divider(
                color = Color.Black,
                modifier = Modifier
                    .width(1.dp)
                    .fillMaxHeight()
            )

            Box(
                modifier = Modifier
                    .weight(1f)
                    .clickable { onNavigateToSettings() },
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Default.Settings, contentDescription = null, tint = Color.Black)
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    val dummyChallenges = listOf(
        Challenge("P", "R", true),
        Challenge("P", "A", false),
        Challenge("P", "J", true),
        Challenge("P", "C", false),
        Challenge("P", "Z", true),
    )

    HomeScreen(
        homeViewModel = viewModel(), // <<-- NEU: ViewModel für die Preview
        challenges = dummyChallenges,
        onChallengeClick = {},
        onNavigateToHome = {},
        onNavigateToParticipants = {},
        onNavigateToSettings = {}
    )
}
