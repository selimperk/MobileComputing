package com.myapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.myapp.model.room.entities.Challenges
import com.myapp.viewmodel.ChallengesViewModel
import com.myapp.viewmodel.SensorViewModel
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun ChallengeDetailScreen(
    challenge: Challenges,
    // Diese ViewModels werden für echte Sensoraktionen genutzt (noch nicht angebunden)
    challengesViewModel: ChallengesViewModel? = null,
    sensorViewModel: SensorViewModel? = null
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF156082))
    ) {
        // Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFFF6F1C))
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(Icons.Default.Home, contentDescription = "Home", tint = Color.Black)
            Spacer(modifier = Modifier.weight(1f))
            Text(
                "ChallengeDetails",
                color = Color.White,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Challenge Titel
        Text(
            text = challenge.title,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            modifier = Modifier.padding(start = 24.dp, bottom = 8.dp)
        )

        // Beschreibung + Kamera-Button in einer Zeile
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(start = 24.dp, end = 24.dp, bottom = 16.dp)
                .fillMaxWidth()
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    "Beschreibung",
                    fontStyle = FontStyle.Italic,
                    color = Color.LightGray,
                    fontSize = 14.sp
                )
                Text(
                    text = challenge.description ?: "",
                    color = Color.White,
                    fontSize = 16.sp,
                    modifier = Modifier.padding(top = 2.dp)
                )
                Divider(color = Color.Black, thickness = 1.dp, modifier = Modifier.padding(top = 8.dp, end = 16.dp))
            }
            IconButton(
                onClick = { /* Kamera starten */ },
                modifier = Modifier
                    .size(48.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.CameraAlt,
                    contentDescription = "Kamera starten",
                    tint = Color.Black,
                    modifier = Modifier.size(36.dp)
                )
            }
        }

        // Bildvorschau (Platzhalter)
        Box(
            modifier = Modifier
                .padding(32.dp)
                .size(120.dp)
                .background(Color(0xFF156082), shape = RoundedCornerShape(12.dp))
                .align(Alignment.CenterHorizontally),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(id = android.R.drawable.ic_menu_gallery),
                contentDescription = "Bildvorschau",
                modifier = Modifier.size(96.dp),
                tint = Color.Black
            )
        }

        // "Foto speichern" Button
        Button(
            onClick = { /* Foto speichern */ },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 48.dp)
                .height(48.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFDD82D4))
        ) {
            Text("Foto speichern", color = Color.White, fontSize = 18.sp)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ChallengeDetailScreenPreview() {
    ChallengeDetailScreen(
        challenge = Challenges(
            id = 1,
            title = "Dornseifer-Challenge",
            description = "Mache ein Foto vor dem Dornseifer in Gummersbach.",
            isDone = false,
            createdAt = System.currentTimeMillis(),
            dueDate = null,
            requiresCamera = true,
            requiresMicrophone = false,
            requiresMotionSensor = false,
            requiresAltitudeSensor = false
        )
    )
}