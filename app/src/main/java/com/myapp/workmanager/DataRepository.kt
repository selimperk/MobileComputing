package com.myapp.workmanager

import android.app.Application
import android.content.Context
import kotlinx.coroutines.delay

class DataRepository (applicationContext : Context) {
    fun syncData(){
        Thread.sleep(3000)
        println("Data synced")
    }
}