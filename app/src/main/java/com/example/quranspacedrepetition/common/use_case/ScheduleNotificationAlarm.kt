package com.example.quranspacedrepetition.common.use_case

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.os.Build
import androidx.core.app.NotificationManagerCompat
import androidx.datastore.core.DataStore
import com.example.quranspacedrepetition.common.di.IoDispatcher
import com.example.quranspacedrepetition.feature_pages.domain.use_case.UpdateReminderNotification
import com.example.quranspacedrepetition.feature_settings.domain.model.UserPreferences
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.first
import timber.log.Timber
import java.time.LocalTime
import java.util.*
import javax.inject.Inject

class ScheduleNotificationAlarm @Inject constructor(
    private val alarmPendingIntent: PendingIntent,
    private val notificationManager: NotificationManagerCompat,
    private val alarmManager: AlarmManager,
    private val dataStore: DataStore<UserPreferences>,
    @IoDispatcher private val dispatcher: CoroutineDispatcher,
) {

    /** USE_EXACT_ALARM permission can substitute for SCHEDULE_EXACT_ALARM */
    @SuppressLint("MissingPermission")
    operator fun invoke() = CoroutineScope(Job() + dispatcher).launch {
        if (!canShowReminderNotification()) {
            Timber.d("invoke: Can't show review reminder notification; exiting alarm use-case")
            return@launch
        }

        val notificationTimePref = dataStore.data.first().notificationTime

        val shouldScheduleToday = LocalTime.now().isBefore(notificationTimePref)
        val dateOffset = if (shouldScheduleToday) 0 else 1
        Timber.d("invoke: notificationTimePref=$notificationTimePref. shouldScheduleToday=$shouldScheduleToday")

        val calendar = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, notificationTimePref.hour)
            set(Calendar.MINUTE, notificationTimePref.minute)
            set(Calendar.SECOND, 0)
            add(Calendar.DATE, dateOffset)
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