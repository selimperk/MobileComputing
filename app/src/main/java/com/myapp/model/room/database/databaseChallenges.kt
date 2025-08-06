package com.myapp.model.room.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.myapp.model.room.daos.ChallengesDao
import com.myapp.model.room.entities.Challenges

@Database(version = 1, entities = [Challenges::class])
abstract class ChallengesDatabase : RoomDatabase() {
    abstract val dao: ChallengesDao

    companion object {
        private const val DB_NAME = "challenges_store"

        @Volatile
        private var INSTANCE: ChallengesDatabase? = null

        fun getInstance(context: Context): ChallengesDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    ChallengesDatabase::class.java,
                    DB_NAME
                ).build().also { INSTANCE = it }
            }
        }
    }
}