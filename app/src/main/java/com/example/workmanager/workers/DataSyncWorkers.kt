package com.example.workmanager.workers

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.workmanager.DataRepository

class DataSyncWorker (val appContext : Context, workerParams : WorkerParameters, private val dataRepository : DataRepository) :
    Worker(appContext, workerParams) {
    override fun doWork(): Result {
        try {
            dataRepository.syncData()
            return Result.success()
        } catch (e : Exception){
            return Result.failure()
        }
    }
}