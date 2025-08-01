package com.myapp.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.myapp.model.database.ParticipantDatabase
import com.myapp.model.database.entities.Participant
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ParticipantViewModel(app: Application) : AndroidViewModel(app) {

    private val db = ParticipantDatabase.getInstance(app.applicationContext)
    private val dao = db.dao

    val allParticipants = dao.getAllParticipants()

    fun searchParticipants(query: String) = dao.searchParticipantsByName("%$query%")

    fun saveParticipant(firstName: String, lastName: String, phone: String, email: String, birthdate: String? = null) {
        val newEntry = Participant(
            firstName = firstName,
            lastName = lastName,
            phone = phone,
            email = email,
            birthdate = birthdate
        )
        viewModelScope.launch(Dispatchers.IO) {
            dao.insertParticipant(newEntry)
        }
    }

    fun deleteParticipant(entry: Participant) {
        viewModelScope.launch(Dispatchers.IO) {
            dao.deleteParticipant(entry)
        }
    }

    fun updateParticipant(entry: Participant) {
        viewModelScope.launch(Dispatchers.IO) {
            dao.updateParticipant(entry)
        }
    }
}
