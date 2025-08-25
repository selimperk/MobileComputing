package com.myapp.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.myapp.model.DataStore
import com.myapp.model.firebase.SettingsFbRepository
import com.myapp.model.firebase.Settingsfb
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch

class SettingsViewModel(app: Application) : AndroidViewModel(app) {

    private val dataStore = DataStore(app.applicationContext)
    private val repo = SettingsFbRepository()

    // ─────────────────────────────────────
    // Flows zum Beobachten der Werte
    val languageFlow: Flow<String?> = dataStore.languageFlow
    val userNameFlow: Flow<String?> = dataStore.userNameFlow
    val emailFlow: Flow<String?> = dataStore.emailFlow
    val notificationFlow: Flow<Boolean> = dataStore.notificationFlow
    val avatarIdFlow: Flow<Int> = dataStore.avatarIdFlow

    // Dummy-UserId (später von Firebase Auth holen)
    private val userId = "testuser"

    // UI-Events (z. B. für Snackbar)
    private val _events = MutableSharedFlow<String>(extraBufferCapacity = 1)
    val events = _events.asSharedFlow()

    // ─────────────────────────────────────
    // Set-Methoden zum lokalen Speichern
    fun setLanguage(languageCode: String) {
        viewModelScope.launch(Dispatchers.IO) { dataStore.setLanguage(languageCode) }
    }

    fun setUserName(name: String) {
        viewModelScope.launch(Dispatchers.IO) { dataStore.setUserName(name) }
    }

    fun setEmail(email: String) {
        viewModelScope.launch(Dispatchers.IO) { dataStore.setEmail(email) }
    }

    fun setNotificationsEnabled(enabled: Boolean) {
        viewModelScope.launch(Dispatchers.IO) { dataStore.setNotificationsEnabled(enabled) }
    }

    fun setAvatarId(id: Int) {
        viewModelScope.launch(Dispatchers.IO) { dataStore.setAvatarId(id) }
    }

    // ─────────────────────────────────────
    // Cloud: Flows hochladen (nutzt aktuell gespeicherte Werte aus DataStore)
    fun uploadSettingsToCloud() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val profile = Settingsfb(
                    userName = userNameFlow.firstOrNull().orEmpty(),
                    email = emailFlow.firstOrNull().orEmpty(),
                    language = languageFlow.firstOrNull().orEmpty(),
                    notificationsEnabled = notificationFlow.first(),
                    avatarId = avatarIdFlow.firstOrNull() ?: 0
                )
                repo.saveSettings(userId, profile)
                _events.tryEmit("Einstellungen in die Cloud gespeichert ✅")
            } catch (t: Throwable) {
                _events.tryEmit("Fehler beim Speichern: ${t.message ?: "Unbekannt"}")
            }
        }
    }


    fun downloadSettingsFromCloud() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val loaded = repo.loadSettings(userId)
                if (loaded != null) {
                    dataStore.setUserName(loaded.userName)
                    dataStore.setEmail(loaded.email)
                    dataStore.setLanguage(loaded.language)
                    dataStore.setNotificationsEnabled(loaded.notificationsEnabled)
                    dataStore.setAvatarId(loaded.avatarId)
                    _events.tryEmit("Einstellungen aus der Cloud geladen ✅")
                } else {
                    _events.tryEmit("Keine Cloud-Daten gefunden")
                }
            } catch (t: Throwable) {
                _events.tryEmit("Fehler beim Laden: ${t.message ?: "Unbekannt"}")
            }
        }
    }
}


