package com.uet.sleeptracker.notifications

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.media.AudioAttributes
import android.media.RingtoneManager
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.uet.sleeptracker.MainActivity
import com.uet.sleeptracker.R

class LumiFirebaseMessagingService : FirebaseMessagingService() {

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        FcmTokenRegistrar.register(token)
    }

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)

        createNotificationChannels()

        val data = message.data
        val type = data["type"] ?: inferType(data)

        val title = message.notification?.title
            ?: data["title"]
            ?: when (type) {
                "alarm" -> "Smart Alarm"
                "habit_nudge" -> "LUMI Reminder"
                else -> "LUMI"
            }

        val body = message.notification?.body
            ?: data["body"]
            ?: data["message"]
            ?: when (type) {
                "alarm" -> "Time to wake up."
                "habit_nudge" -> "You have a sleep habit reminder."
                else -> "You have a new notification."
            }

        val channelId = when (type) {
            "alarm" -> CHANNEL_ALARMS
            else -> CHANNEL_HABIT_NUDGES
        }

        showNotification(channelId, title, body)
    }

    private fun inferType(data: Map<String, String>): String {
        return when {
            data.containsKey("alarm_id") -> "alarm"
            data.containsKey("habit_id") -> "habit_nudge"
            else -> "default"
        }
    }

    private fun showNotification(channelId: String, title: String, body: String) {
        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
        }

        val pendingIntent = PendingIntent.getActivity(
            this,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val priority = if (channelId == CHANNEL_ALARMS) {
            NotificationCompat.PRIORITY_HIGH
        } else {
            NotificationCompat.PRIORITY_DEFAULT
        }

        val builder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.noti_icon)
            .setContentTitle(title)
            .setContentText(body)
            .setStyle(NotificationCompat.BigTextStyle().bigText(body))
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .setPriority(priority)

        if (channelId == CHANNEL_ALARMS) {
            builder
                .setCategory(NotificationCompat.CATEGORY_ALARM)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM))
                .setVibrate(longArrayOf(0, 500, 300, 500))
        }

        NotificationManagerCompat.from(this).notify(
            System.currentTimeMillis().toInt(),
            builder.build()
        )
    }

    private fun createNotificationChannels() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) return

        val manager = getSystemService(NotificationManager::class.java)

        val habitChannel = NotificationChannel(
            CHANNEL_HABIT_NUDGES,
            "Habit reminders",
            NotificationManager.IMPORTANCE_DEFAULT
        ).apply {
            description = "Sleep habit reminders from LUMI"
        }

        val alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)
        val alarmAttributes = AudioAttributes.Builder()
            .setUsage(AudioAttributes.USAGE_ALARM)
            .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
            .build()

        val alarmChannel = NotificationChannel(
            CHANNEL_ALARMS,
            "Smart alarms",
            NotificationManager.IMPORTANCE_HIGH
        ).apply {
            description = "Smart alarm notifications"
            enableVibration(true)
            setSound(alarmSound, alarmAttributes)
        }

        manager.createNotificationChannel(habitChannel)
        manager.createNotificationChannel(alarmChannel)
    }

    companion object {
        const val CHANNEL_HABIT_NUDGES = "habit_nudges"
        const val CHANNEL_ALARMS = "alarms"
    }
}
