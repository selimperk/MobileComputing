package com.myapp.model.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.myapp.model.database.daos.ParticipantDao
import com.myapp.model.database.entities.Participant

@Database(version = 1, entities = [Participant::class])
abstract class ParticipantDatabase : RoomDatabase() {
    abstract val dao: ParticipantDao

    companion object {
        private const val DB_NAME = "participant_store"

        @Volatile
        private var INSTANCE: ParticipantDatabase? = null

        fun getInstance(context: Context): ParticipantDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    ParticipantDatabase::class.java,
                    DB_NAME
                ).build().also { INSTANCE = it }
            }
        }
    }
}
