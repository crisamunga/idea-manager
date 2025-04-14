package com.interview.ideamanager.workers

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.work.Constraints
import androidx.work.CoroutineWorker
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.ExistingWorkPolicy
import androidx.work.Operation
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.interview.ideamanager.IdeaManagerApplication
import com.interview.ideamanager.R
import kotlinx.coroutines.coroutineScope
import java.time.Duration
import java.time.LocalDate
import java.time.ZonedDateTime
import java.util.concurrent.TimeUnit

class ReminderWorker(context: Context, workerParams: WorkerParameters) : CoroutineWorker(context, workerParams) {
    override suspend fun doWork(): Result {
        if (ContextCompat.checkSelfPermission(
            applicationContext,
            Manifest.permission.POST_NOTIFICATIONS
        ) == PackageManager.PERMISSION_GRANTED) {


            val application = applicationContext as IdeaManagerApplication
            val today = LocalDate.now()
            val taskRepository = application.appContainer.taskRepository

            taskRepository.getPendingTasksByDueDate(today).collect() { tasks ->
                if (tasks.isNotEmpty()) {
                    val title = application.getString(R.string.tasks_due_today)
                    val message = application.getString(R.string.you_have_tasks_due_today, tasks.size)
                    showNotification(title, message)
                }
            }
            return Result.success()
        } else {
            // Handle the case where the permission is not granted
            return Result.success()
        }
    }

    private fun showNotification(title: String, message: String) {
        val notificationManager = applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val channelId = "scheduled_events"

        val channel = NotificationChannel(channelId, "Tasks due", NotificationManager.IMPORTANCE_DEFAULT)
        notificationManager.createNotificationChannel(channel)

        val notification = NotificationCompat.Builder(applicationContext, channelId)
            .setContentTitle(title)
            .setContentText(message)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .build()

        notificationManager.notify(1, notification)
    }

    companion object {
        fun enqueueWork(context: Context): Operation {
            val now = ZonedDateTime.now()
            val targetTime = now.withHour(8).withMinute(0).withSecond(0).withNano(0)
            val initialDelay = if (now.isAfter(targetTime)) {
                Duration.between(now, targetTime.plusDays(1)).toMinutes()
            } else {
                Duration.between(now, targetTime).toMinutes()
            }

            val constraints = Constraints.Builder()
                .setRequiresBatteryNotLow(false)
                .setRequiresDeviceIdle(false)
                .setRequiresCharging(false)
                .build()

            val workRequest = PeriodicWorkRequestBuilder<ReminderWorker>(1, TimeUnit.DAYS)
                .setInitialDelay(initialDelay, TimeUnit.MINUTES)
                .setConstraints(constraints)
                .build()
            return WorkManager.getInstance(context).enqueueUniquePeriodicWork("reminder-task", ExistingPeriodicWorkPolicy.CANCEL_AND_REENQUEUE, workRequest)
        }
    }
}