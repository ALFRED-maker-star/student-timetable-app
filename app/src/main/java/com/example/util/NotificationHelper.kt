package com.example.util

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.R
import com.example.data.model.Course

object NotificationHelper {
    private const val CHANNEL_ID = "classmate_reminders"
    private const val CHANNEL_NAME = "Class Reminders"

    fun createNotificationChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Reminders for upcoming university classes"
                enableVibration(true)
            }
            val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            manager.createNotificationChannel(channel)
        }
    }

    fun showClassReminder(context: Context, course: Course, leadMinutes: Int = 10) {
        createNotificationChannel(context)

        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle("Class in $leadMinutes min: ${course.code}")
            .setContentText("${course.name} in ${course.classroom} at ${course.formattedStartTime()}")
            .setStyle(
                NotificationCompat.BigTextStyle()
                    .bigText(
                        "Class: ${course.name} (${course.code})\n" +
                                "Time: ${course.formattedTimeRange()}\n" +
                                "Room: ${course.classroom}\n" +
                                "Lecturer: ${course.lecturer}"
                    )
            )
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .build()

        try {
            NotificationManagerCompat.from(context).notify(course.id.toInt().coerceAtLeast(100), notification)
        } catch (_: SecurityException) {
            // Handled gracefully if permission denied
        }
    }
}
