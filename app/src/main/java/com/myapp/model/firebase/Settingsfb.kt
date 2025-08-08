package com.myapp.model.firebase

data class Settingsfb (
    val userName: String = "",
    val email: String = "",
    val language: String = "",
    val notificationsEnabled: Boolean = false,
    val avatarId: Int = 0
)