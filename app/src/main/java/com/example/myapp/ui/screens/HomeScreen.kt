package com.example.myapp.ui.screens

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import com.example.myapp.model.Challenge
import com.example.myapp.ui.composables.ChallengeCard

@Composable
fun HomeScreen(
    challenges: List<Challenge>,
    onChallengeClick: (Challenge) -> Unit
) {
    LazyColumn {
        items(challenges) { challenge ->
            ChallengeCard(
                player1 = challenge.player1,
                player2 = challenge.player2,
                won = challenge.won,
                onClick = { onChallengeClick(challenge) }
            )
        }
    }
}

/*
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
*/