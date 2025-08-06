package com.myapp.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.myapp.model.room.database.ChallengesDatabase
import com.myapp.model.room.entities.Challenges
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ChallengesViewModel(app: Application) : AndroidViewModel(app) {

    private val db = ChallengesDatabase.getInstance(app.applicationContext)
    private val dao = db.dao

    // Live-Liste aller Challenges als Flow
    val allChallenges = dao.getAllChallenges()

    // Suche Challenges nach Titel (optional)
    fun searchChallenges(query: String) = dao.searchChallengesByName("%$query%")

    // Neue Challenge speichern
    fun saveChallenge(
        title: String,
        description: String? = null,
        isDone: Boolean = false,
        createdAt: Long = System.currentTimeMillis(),
        dueDate: Long? = null
    ) {
        val newEntry = Challenges(
            title = title,
            description = description,
            isDone = isDone,
            createdAt = createdAt,
            dueDate = dueDate
        )
        viewModelScope.launch(Dispatchers.IO) {
            dao.insertChallenge(newEntry)
        }
    }

    // Challenge löschen
    fun deleteChallenge(entry: Challenges) {
        viewModelScope.launch(Dispatchers.IO) {
            dao.deleteChallenge(entry)
        }
    }

    // Challenge aktualisieren
    fun updateChallenge(entry: Challenges) {
        viewModelScope.launch(Dispatchers.IO) {
            dao.updateChallenge(entry)
        }
    }
}
