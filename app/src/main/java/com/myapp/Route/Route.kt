package com.myapp.Route

import kotlinx.serialization.Serializable

sealed class Route

@Serializable
data object HomeRoute : Route()

@Serializable
data object ParticipantRoute : Route()

@Serializable
data object SettingsRoute : Route()

@Serializable
data object ChallengesRoute : Route()

@Serializable
data class ChallengeDetailRoute(
    val id: Int,
    val title: String,
    val description: String?
) : Route() {
    fun toChallenge() = com.myapp.model.room.entities.Challenges(
        id = id,
        title = title,
        description = description,
        isDone = false,
        createdAt = System.currentTimeMillis(),
        dueDate = null,
        requiresCamera = true,
        requiresMicrophone = false,
        requiresMotionSensor = false,
        requiresAltitudeSensor = false
    )
}
