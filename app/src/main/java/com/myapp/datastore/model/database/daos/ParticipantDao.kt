package com.myapp.datastore.model.database.daos

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.myapp.datastore.model.database.entities.Participant
import kotlinx.coroutines.flow.Flow

@Dao
interface ParticipantDao {

    @Insert
    fun insertParticipant(participant: Participant)

    @Update
    fun updateParticipant(participant: Participant)

    @Delete
    fun deleteParticipant(participant: Participant)

    @Query("SELECT * FROM participants ORDER BY lastname ASC")
    fun getAllParticipants(): Flow<List<Participant>>

    @Query("SELECT * FROM participants WHERE firstname LIKE :search OR lastname LIKE :search")
    fun searchParticipantsByName(search: String): Flow<List<Participant>>
}
