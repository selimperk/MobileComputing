package com.example.persistence.model

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private const val PREFERENCES_NAME = "user_preferences"

// Extension Property für den Context
private val Context.dataStore by preferencesDataStore(PREFERENCES_NAME)

class DataStore(context: Context) {

    private val dataStore = context.dataStore

    companion object {
        val LANGUAGE_KEY = stringPreferencesKey("language")
        val USERNAME_KEY = stringPreferencesKey("user_name")
        val EMAIL_KEY = stringPreferencesKey("email")
        val NOTIFICATION_KEY = booleanPreferencesKey("notifications_enabled")
    }

    // Sprache
    val languageFlow: Flow<String?> = dataStore.data.map { it[LANGUAGE_KEY] }

    suspend fun setLanguage(languageCode: String) {
        dataStore.edit { it[LANGUAGE_KEY] = languageCode }
    }

    // Benutzername
    val userNameFlow: Flow<String?> = dataStore.data.map { it[USERNAME_KEY] }

    suspend fun setUserName(name: String) {
        dataStore.edit { it[USERNAME_KEY] = name }
    }

    // E-Mail
    val emailFlow: Flow<String?> = dataStore.data.map { it[EMAIL_KEY] }

    suspend fun setEmail(email: String) {
        dataStore.edit { it[EMAIL_KEY] = email }
    }

    // Push-Benachrichtigungen
    val notificationFlow: Flow<Boolean> = dataStore.data.map { it[NOTIFICATION_KEY] ?: false }

    suspend fun setNotificationsEnabled(enabled: Boolean) {
        dataStore.edit { it[NOTIFICATION_KEY] = enabled }
    }
}
