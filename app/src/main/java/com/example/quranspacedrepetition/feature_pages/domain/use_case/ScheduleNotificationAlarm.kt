package com.example.quranspacedrepetition.feature_pages.domain.use_case

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.os.Build
import androidx.core.app.NotificationManagerCompat
import timber.log.Timber
import java.util.*
import javax.inject.Inject

class ScheduleNotificationAlarm @Inject constructor(
    private val alarmPendingIntent: PendingIntent,
    private val notificationManager: NotificationManagerCompat,
    private val alarmManager: AlarmManager,
) {

    /** USE_EXACT_ALARM permission can substitute for SCHEDULE_EXACT_ALARM */
    @SuppressLint("MissingPermission")
    operator fun invoke() {
        if (!canShowReminderNotification()) {
            Timber.d("invoke: Can't show review reminder notification; exiting alarm use-case")
            return
        }

        val calendar = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            add(Calendar.DATE, 1)
        }

        alarmManager.setExact(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            alarmPendingIntent
        )
    }

    /** Permission check already handled */
    @SuppressLint("NewApi")
    private fun canShowReminderNotification(): Boolean {
        fun NotificationChannel.isChannelEnabled(): Boolean {
            return importance != NotificationManager.IMPORTANCE_NONE
        }

        val isOreoOrLater = Build.VERSION.SDK_INT >= Build.VERSION_CODES.O
        val notificationChannel =
            notificationManager.getNotificationChannel(UpdateReminderNotification.REMINDER_NOTIFICATION_CHANNEL_ID)
        val notificationsEnabled = notificationManager.areNotificationsEnabled()

        return when (isOreoOrLater) {
            true -> notificationsEnabled && notificationChannel?.isChannelEnabled() == true
            false -> notificationsEnabled
        }
    }
}