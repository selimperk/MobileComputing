package com.myapp.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.work.*
import com.myapp.worker.DataSyncWorker
import javax.inject.Inject
import java.util.concurrent.TimeUnit
import dagger.hilt.android.lifecycle.HiltViewModel

@HiltViewModel
class DataViewModel @Inject constructor(
    app: Application
) : AndroidViewModel(app) {
    private val workManager = WorkManager.getInstance(app)
    private val PERIODIC_DATA_SYNC_WORK_NAME = "PERIODIC_DATA_SYNC"

    fun initDataSync() {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .setRequiresStorageNotLow(true)
            .build()

        val request = OneTimeWorkRequestBuilder<DataSyncWorker>()
            .setConstraints(constraints)
            .build()

        workManager.enqueue(request)
    }

    fun initPeriodicDataSync() {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .setRequiresStorageNotLow(true)
            .build()

        val periodicRequest = PeriodicWorkRequestBuilder<DataSyncWorker>(15, TimeUnit.MINUTES)
            .setConstraints(constraints)
            .build()

        workManager.enqueueUniquePeriodicWork(
            PERIODIC_DATA_SYNC_WORK_NAME,
            ExistingPeriodicWorkPolicy.KEEP,
            periodicRequest
        )
    }

    fun cancelPeriodicDataSync() {
        workManager.cancelUniqueWork(PERIODIC_DATA_SYNC_WORK_NAME)
    }
}
