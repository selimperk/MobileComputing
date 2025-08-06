package com.myapp.model.room.daos

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.myapp.model.room.entities.Challenges
import kotlinx.coroutines.flow.Flow

@Dao
interface ChallengesDao {

    // Fügt eine neue Challenge in die Datenbank ein
    @Insert
    suspend fun insertChallenge(challenge: Challenges)

    // Aktualisiert eine bestehende Challenge (z. B. Name, Status, etc.)
    @Update
    suspend fun updateChallenge(challenge: Challenges)

    // Löscht eine Challenge aus der Datenbank
    @Delete
    suspend fun deleteChallenge(challenge: Challenges)

    // Gibt alle Challenges als Liste zurück, nach ID absteigend sortiert
    @Query("SELECT * FROM challenges ORDER BY id DESC")
    fun getAllChallenges(): Flow<List<Challenges>>

    // Holt eine Challenge mit einer bestimmten ID
    @Query("SELECT * FROM challenges WHERE id = :challengeId LIMIT 1")
    suspend fun getChallengeById(challengeId: Int): Challenges?

    // Sucht nach Challenges, deren Titel das Suchwort enthält
    @Query("SELECT * FROM challenges WHERE title LIKE :query ORDER BY id DESC")
    fun searchChallengesByName(query: String): Flow<List<Challenges>>
}