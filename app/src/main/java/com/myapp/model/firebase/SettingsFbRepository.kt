package com.myapp.model.firebase

import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await

class SettingsFbRepository {
    private val db = Firebase.firestore

    suspend fun saveSettings(userId: String, settings: Settingsfb) {
        db.collection("settings").document(userId).set(settings).await()
    }

    suspend fun loadSettings(userId: String): Settingsfb? {
        val snapshot = db.collection("settings").document(userId).get().await()
        return snapshot.toObject(Settingsfb::class.java)
    }
}