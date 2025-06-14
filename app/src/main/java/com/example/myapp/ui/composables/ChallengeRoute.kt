package com.example.myapp.ui.composables

import kotlinx.serialization.Serializable

@Serializable
data class ChallengeRoute(
    val player1: String,
    val player2: String,
    val won: Boolean
) : Route()
