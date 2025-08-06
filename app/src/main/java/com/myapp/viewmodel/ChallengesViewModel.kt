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
    private val dataSyncRepository: DataSyncRepository // <<-- NEU: Deklariere das Repository

    // <<-- NEU: Initialisiere das DataSyncRepository im init-Block
    init {
        dataSyncRepository = DataSyncRepository(app.applicationContext) // Oder nutze eine DI-Bibliothek
        // Starte die Synchronisation direkt beim Initialisieren des ViewModels
        // Dies ist ein guter Ort, wenn die Daten beim App-Start oder ersten Screen-Laden synchronisiert werden sollen
        syncChallenges()
    }

    // Live-Liste aller Challenges als Flow
    val allChallenges = dao.getAllChallenges()

    // <<-- NEU: StateFlow zur Anzeige des Synchronisierungsstatus in der UI
    private val _isSyncing = MutableStateFlow(false)
    val isSyncing: StateFlow<Boolean> = _isSyncing.asStateFlow()

    // <<-- NEU: Funktion zum Starten der Daten-Synchronisation
    fun syncChallenges() {
        viewModelScope.launch(Dispatchers.IO) { // Wichtig: Im IO-Dispatcher ausführen
            try {
                _isSyncing.value = true // Setze den Status auf "wird synchronisiert"
                Log.d("ChallengesViewModel", "Starte Datensynchronisation...")
                dataSyncRepository.syncData() // Rufe die syncData-Funktion deines Repositories auf
                Log.d("ChallengesViewModel", "Datensynchronisation abgeschlossen.")
            } catch (e: Exception) {
                Log.e("ChallengesViewModel", "Fehler bei der Datensynchronisation: ${e.message}", e)
                // Hier könntest du einen weiteren StateFlow für Fehlermeldungen hinzufügen
            } finally {
                _isSyncing.value = false // Setze den Status zurück auf "Synchronisation beendet"
            }
        }
    }

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
            // Optional: Nach dem Speichern auch synchronisieren, um Daten zum Server zu senden
            // dataSyncRepository.syncData()
        }
    }

    // Challenge löschen
    fun deleteChallenge(entry: Challenges) {
        viewModelScope.launch(Dispatchers.IO) {
            dao.deleteChallenge(entry)
            // Optional: Nach dem Löschen auch synchronisieren
            // dataSyncRepository.syncData()
        }
    }

    // Challenge aktualisieren
    fun updateChallenge(entry: Challenges) {
        viewModelScope.launch(Dispatchers.IO) {
            dao.updateChallenge(entry)
            // Optional: Nach dem Aktualisieren auch synchronisieren
            // dataSyncRepository.syncData()
        }
    }
}