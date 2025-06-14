package com.example.myapp.Route

sealed class Route

data object HomeRoute : Route()
data object AddRoute : Route()
data object SettingsRoute : Route()