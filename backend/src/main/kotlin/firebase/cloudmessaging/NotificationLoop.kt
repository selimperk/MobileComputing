package com.myapp.firebase.cloudmessaging

import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.Message
import io.ktor.server.application.Application
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.seconds

fun Application.loopNotifications() {
    launch {
        while (isActive) {
            delay(60.seconds)

            val messagesToSend = fcmTokens.keys.map { token ->
                Message.builder()
                    .putData("message", "ping")
                    .setToken(token)
                    .build()
            }

            if (messagesToSend.isEmpty()) {
                println("no messages sent")
                continue
            }

            println("Attempting to send ${messagesToSend.size} messages...")
            try {
                val response = FirebaseMessaging.getInstance().sendEachAsync(messagesToSend).get()
                println("Successfully sent ${response.successCount} messages, ${response.failureCount} messages failed.")

                if (response.failureCount > 0) {
                    response.responses.forEachIndexed { index, result ->
                        if (!result.isSuccessful) {
                            println("Failed to send: ${result.exception.message}")
                        }
                    }
                }
            } catch (e: Exception) {
                println("Error sending FCM messages: ${e.message}")
            }
        }
    }
}