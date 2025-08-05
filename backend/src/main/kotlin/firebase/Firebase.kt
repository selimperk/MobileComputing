package com.myapp.firebase

import com.google.auth.oauth2.GoogleCredentials
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import io.ktor.server.application.Application
import java.io.FileInputStream

fun Application.initFirebase() {
    val serviceAccount = FileInputStream("serviceAccountKey.json")

    val options = FirebaseOptions.builder()
        .setCredentials(GoogleCredentials.fromStream(serviceAccount))
        .build()

    try {
        FirebaseApp.initializeApp(options)
        println("Firebase Admin SDK successfully initialized.")
    } catch (e: Exception) {
        println("Error initializing Firebase Admin SDK: ${e.message}")
    }
}