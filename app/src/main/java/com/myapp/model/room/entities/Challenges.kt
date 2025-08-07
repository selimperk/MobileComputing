package com.myapp.model.room.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "challenges")
data class Challenges(
    @PrimaryKey(autoGenerate = true) val id: Long? = null,

    // Titel der Challenge
    val title: String,

    // Beschreibung, der Challenge
    val description: String? = null,

    // Status: Ist die Challenge erledigt?
    val isDone: Boolean = false,

    // Wann wurde die Challenge erstellt? (z.B. als Zeitstempel)
    val createdAt: Long = System.currentTimeMillis(),

    // Bis wann läuft die Challenge? (Deadline)
    val dueDate: Long? = null,

    //Sensoren
    val requiresCamera: Boolean = false,
    val requiresMicrophone: Boolean = false,
    val requiresMotionSensor: Boolean = false,
    val requiresAltitudeSensor: Boolean = false


)