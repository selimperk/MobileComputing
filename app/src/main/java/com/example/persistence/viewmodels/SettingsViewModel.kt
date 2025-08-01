package com.example.persistence.viewmodels


import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.persistence.model.DataStore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class SettingsViewModel(app: Application) : AndroidViewModel(app) {

    private val dataStore = DataStore(app.applicationContext)

    // ─────────────────────────────────────
    // Flows zum Beobachten der Werte
    val languageFlow: Flow<String?> = dataStore.languageFlow
    val userNameFlow: Flow<String?> = dataStore.userNameFlow
    val emailFlow: Flow<String?> = dataStore.emailFlow
    val notificationFlow: Flow<Boolean> = dataStore.notificationFlow

    // ─────────────────────────────────────
    // Set-Methoden zum Speichern
    fun setLanguage(languageCode: String) {
        viewModelScope.launch(Dispatchers.IO) {
            dataStore.setLanguage(languageCode)
        }
    }

    fun setUserName(name: String) {
        viewModelScope.launch(Dispatchers.IO) {
            dataStore.setUserName(name)
        }
    }

    fun setEmail(email: String) {
        viewModelScope.launch(Dispatchers.IO) {
            dataStore.setEmail(email)
        }
    }

    fun setNotificationsEnabled(enabled: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            dataStore.setNotificationsEnabled(enabled)
        }
    }
}
