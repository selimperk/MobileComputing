package com.example.myapp.workers

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.myapp.DataSyncRepository

class DataSyncWorker(
    context: Context,
    params: WorkerParameters
) : Worker(context, params) {

    override fun doWork(): Result {
        val repo = DataSyncRepository(applicationContext)
        repo.syncData()
        return Result.success()
    }
}
