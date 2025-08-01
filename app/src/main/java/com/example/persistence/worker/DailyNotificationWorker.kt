package com.example.myapp.worker

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.work.Worker
import androidx.work.WorkerParameters

class DailyNotificationWorker(
    private val context: Context,
    workerParams: WorkerParameters
) : Worker(context, workerParams) {

    override fun doWork(): Result {
        createNotificationChannel()
        showNotification()
        return Result.success()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "DailyChallengeChannel"
            val descriptionText = "Tägliche Benachrichtigung für neue Challenges"
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel("daily_challenge_channel", name, importance).apply {
                description = descriptionText
            }
            val notificationManager: NotificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun showNotification() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
            ContextCompat.checkSelfPermission(
                context,
                android.Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // Berechtigung nicht vorhanden → keine Notification anzeigen
            return
        }

        val notification = NotificationCompat.Builder(context, "daily_challenge_channel")
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentTitle("Neue Challenges verfügbar!")
            .setContentText("Öffne die App und entdecke die neuen Aufgaben.")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .build()

        with(NotificationManagerCompat.from(context)) {
            notify(System.currentTimeMillis().toInt(), notification)
        }
    }

}
