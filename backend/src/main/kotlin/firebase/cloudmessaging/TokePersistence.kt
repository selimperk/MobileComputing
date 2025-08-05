package com.myapp.firebase.cloudmessaging

import java.io.File

val TOKENS_FILE = File("fcm_tokens.txt")

fun loadTokensFromFile() {
    if (TOKENS_FILE.exists()) {
        try {
            TOKENS_FILE.readLines().forEach { token ->
                if (token.isNotBlank()) {
                    fcmTokens[token] = Unit
                }
            }
            println(" ${fcmTokens.size} tokens loaded from file.")
        } catch (e: Exception) {
            println("Error loading tokens from file: ${e.message}")
        }
    } else {
        println("Tokens file does not exist. Starting with no tokens.")
    }
}

fun saveTokensToFile() {
    try {
        TOKENS_FILE.writeText(fcmTokens.keys.joinToString("\n"))
    } catch (e: Exception) {
        println("Error saving tokens to file: ${e.message}")
    }
}