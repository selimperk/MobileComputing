package com.example.myapp.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.myapp.model.Challenge
import com.example.myapp.ui.composables.ChallengeCard




@Composable
fun HomeScreen(challenges: List<Challenge>) {
    LazyColumn {
        items(challenges) { challenge ->
            ChallengeCard(
                player1 = challenge.player1,
                player2 = challenge.player2,
                won = challenge.won
            )
        }
    }
}
@Preview
@Composable
fun ChallengePreview() {
    HomeScreen(
        listOf(
            Challenge("P", "R", true),
            Challenge("P", "A", false),
            Challenge("P", "J", true),
            Challenge("P", "C", false),
            Challenge("P", "Z", true),
        )
    )
}
