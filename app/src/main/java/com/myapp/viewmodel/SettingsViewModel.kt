package com.myapp.viewmodel


import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.myapp.model.DataStore
import com.myapp.model.firebase.SettingsFbRepository
import com.myapp.model.firebase.Settingsfb
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
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

    //Dummy-UserId (später von Firebase Auth holen)
    private val userId = "testuser"

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

    fun setAvatarId(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            dataStore.setAvatarId(id)
        }
    }

    fun uploadSettingsToCloud() {
        viewModelScope.launch(Dispatchers.IO) {
            val profile = Settingsfb(
                userName = userNameFlow.first() ?: "",
                email = emailFlow.first() ?: "",
                language = languageFlow.first() ?: "",
                notificationsEnabled = notificationFlow.first(),
                avatarId = 0 // Passe ggf. an!
            )
            repo.saveSettings(userId, profile)
        }
    }

    fun downloadSettingsFromCloud() {
        viewModelScope.launch(Dispatchers.IO) {
            val loaded = repo.loadSettings(userId)
            loaded?.let {
                dataStore.setUserName(it.userName)
                dataStore.setEmail(it.email)
                dataStore.setLanguage(it.language)
                dataStore.setNotificationsEnabled(it.notificationsEnabled)
                // Optional: dataStore.setAvatarId(it.avatarId)
            }
        }
    }
}
