import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.draw.clip

// Datenklasse für eine Challenge
data class Challenge(
    val opponentName: String,
    val isPlayerWinning: Boolean
)

// Liste aller Challenges
@Composable
fun ChallengeList(challenges: List<Challenge>) {
    Column(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp)
    ) {
        challenges.forEach { challenge ->
            ChallengeRow(
                opponentInitial = challenge.opponentName.first().uppercaseChar().toString(),
                isPlayerWinning = challenge.isPlayerWinning
            )
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

// Eine einzelne Zeile (Spieler vs Gegner + Emoji)
@Composable
fun ChallengeRow(opponentInitial: String, isPlayerWinning: Boolean) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFFEFEFEF))
            .clip(RoundedCornerShape(12.dp))
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            InitialCircle(letter = "P") // eigener Spieler
            Spacer(modifier = Modifier.width(8.dp))
            Text("vs", style = MaterialTheme.typography.bodyMedium)
            Spacer(modifier = Modifier.width(8.dp))
            InitialCircle(letter = opponentInitial) // Gegner
        }

        // Emoji: 👍 oder ❌
        Text(
            text = if (isPlayerWinning) "👍" else "❌",
            fontSize = 20.sp
        )
    }
}

// Der Buchstaben-Kreis
@Composable
fun InitialCircle(letter: String) {
    Box(
        modifier = Modifier
            .size(40.dp)
            .clip(CircleShape)
            .background(Color.Gray),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = letter,
            color = Color.White,
            style = MaterialTheme.typography.bodyMedium.copy(fontSize = 16.sp)
        )
    }
}

// Vorschau zum Testen
@Preview(showBackground = true)
@Composable
fun PreviewChallengeList() {
    val challenges = listOf(
        Challenge("Rebecca", true),
        Challenge("Ali", false),
        Challenge("Julia", true),
        Challenge("Carlos", false),
        Challenge("Zoe", true)
    )
    ChallengeList(challenges)
}

