package com.myapp.Route

sealed class Route

data object HomeRoute : Route()
data object ParticipantRoute : Route()
data object SettingsRoute : Route()

// ChallengeRoute mit Argumenten
data class ChallengeRoute(
    val player1: String,
    val player2: String,
    val won: Boolean
) : Route()