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

    val languageFlow: Flow<String?> = dataStore.languageFlow
    val userNameFlow: Flow<String?> = dataStore.userNameFlow

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
}
