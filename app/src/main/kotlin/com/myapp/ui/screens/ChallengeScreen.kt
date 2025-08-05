package com.myapp.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ChallengeScreen(player1: String, player2: String, won: Boolean) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Spieler 1: $player1")
        Spacer(modifier = Modifier.height(8.dp))
        Text("Spieler 2: $player2")
        Spacer(modifier = Modifier.height(8.dp))
        Text("Status: ${if (won) "Challenge bestanden" else "Noch offen"}")
    }
}