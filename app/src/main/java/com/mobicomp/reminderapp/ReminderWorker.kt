package com.mobicomp.reminderapp

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import kotlin.random.Random


class ReminderWorker(appContext: Context, workerParams: WorkerParameters):
    Worker(appContext, workerParams) {
    override fun doWork(): Result {

        val db = DBHandler(applicationContext)

        // Receiving reminder data from NewReminderActivity
        val message = inputData.getString("message")
        val imgId = inputData.getInt("imgId", 0)
        val date = inputData.getString("date")
        val userId = inputData.getInt("userId", -1)
        val showNotification = inputData.getBoolean("showNotification", true)
        val timingRequirement = inputData.getInt("timingRequirement", 1)

        val remId = db.getReminderId2(message.toString(), date.toString(), imgId, userId)
        val reminderInRange = db.reminderInRange(userId, remId)


        // Displaying a notification & making reminder visible in listview
        if (message != null && date != null && showNotification && timingRequirement != 0 && reminderInRange) {
            showNotification(applicationContext, message, imgId, date)
        }
        if (message != null && date != null && userId != -1 && reminderInRange) {
            db.makeReminderVisible(message, date, imgId, userId)
        }

        return Result.success()
    }

    private fun showNotification(context : Context, message : String, imgId : Int, date : String) {

        val channelID = "REMINDER_APP_NOTIFICATION_CHANNEL"
        var notificationId = Random.nextInt(10, 1000) + 5

        var notificationBuilder = NotificationCompat.Builder(context, channelID)
                .setSmallIcon(imgId)
                .setContentTitle(message)
                .setContentText("Due: $date")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                    channelID,
                    context.getString(R.string.app_name),
                    NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = context.getString(R.string.app_name)
            }
            notificationManager.createNotificationChannel(channel)

        }
        notificationManager.notify(notificationId, notificationBuilder.build())
    }
}

