package com.example.myapp.ui.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.myapp.ui.theme.HandsOn1stTheme


@Composable
fun ChallengeCard(
    player1: String,
    player2: String,
    won: Boolean,
    onClick: () -> Unit = {} // <-- NEU
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp)
            .clickable { onClick() }, // <-- NEU
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row {
            Circle(player1)
            Text(" vs ")
            Circle(player2)
        }
        Icon(
            imageVector = if (won) Icons.Default.ThumbUp else Icons.Default.Close,
            contentDescription = if (won) "Gewonnen" else "Verloren",
            tint = if (won) Color(0xFFFFC107) else Color.Red
        )
    }
}


@Composable
fun Circle(letter: String) {
    Box(
        modifier = Modifier
            .size(40.dp)
            .clip(CircleShape)
            .background(Color.Gray),
        contentAlignment = Alignment.Center
    ) {
        Text(letter.first().toString(), color = Color.White)
    }
}


@Preview(showBackground = true)
@Composable
fun ChallengeCardPreview() {
    HandsOn1stTheme {
        ChallengeCard(
            player1 = "Paul",
            player2 = "Lisa",
            won = true
        )
    }
}
