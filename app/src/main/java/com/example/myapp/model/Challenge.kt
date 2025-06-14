package com.example.myapp.model

import kotlinx.serialization.Serializable

@Serializable
data class Challenge(
    val player1: String,
    val player2: String,
    val status: String // "done", "failed", "open"
)

