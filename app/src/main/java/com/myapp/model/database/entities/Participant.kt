package com.myapp.model.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "participants")
data class Participant(
    @PrimaryKey(autoGenerate = true) val id: Long? = null,
    val firstName: String,
    val lastName: String,
    val phone: String,
    val email: String,
    val birthdate: String? = null // z.B. "1997-05-22"
)
