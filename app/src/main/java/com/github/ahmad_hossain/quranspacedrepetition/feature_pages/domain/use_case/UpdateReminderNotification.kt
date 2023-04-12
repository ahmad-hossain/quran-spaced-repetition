package com.github.ahmad_hossain.quranspacedrepetition.feature_pages.domain.use_case

import android.Manifest
import android.app.AlarmManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.github.ahmad_hossain.quranspacedrepetition.R
import com.github.ahmad_hossain.quranspacedrepetition.common.use_case.ScheduleNotificationAlarm
import com.github.ahmad_hossain.quranspacedrepetition.feature_pages.domain.repository.PageRepository
import comgithubahmadhossainquranspacedrepetition.Page
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

class UpdateReminderNotification @Inject constructor(
    @ApplicationContext private val context: Context,
    private val repository: PageRepository,
    private val reminderNotificationBuilder: NotificationCompat.Builder,
    private val notificationManager: NotificationManagerCompat,
    private val scheduleNotificationAlarm: ScheduleNotificationAlarm,
    private val alarmManager: AlarmManager,
) {

    suspend operator fun invoke() = withContext(Dispatchers.IO) {
        Timber.d("invoke()")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S && !alarmManager.canScheduleExactAlarms()) {
            Timber.d("invoke: Can't schedule exact alarms; exiting use-case")
            return@withContext
        }

        val duePages = repository.getPagesDueToday().first()
        Timber.d("onReceive: ${duePages.size} pages due today")

        withContext(Dispatchers.Main) {
            updateNotificationBuilder(duePages)
            sendNotification()
            scheduleNotificationAlarm()
        }
    }

    private fun updateNotificationBuilder(duePages: List<Page>) {
        val isNotificationOngoing = duePages.isNotEmpty()
        val contentText = when (duePages.isEmpty()) {
            true -> context.getString(R.string.reminder_notification_text_no_pages_due)
            false -> context.getString(R.string.reminder_notification_text, duePages.size)
        }
        Timber.d("updateNotificationBuilder: isNotificationOngoing=$isNotificationOngoing, contentText=$contentText")
        reminderNotificationBuilder
            .setOngoing(isNotificationOngoing)
            .setContentText(contentText)
    }

    private fun sendNotification() {
        val notificationPermissionGranted = ActivityCompat
            .checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
        Timber.d("sendNotification: notificationPermissionGranted=$notificationPermissionGranted")
        if (notificationPermissionGranted) {
            notificationManager.notify(
                REMINDER_NOTIFICATION_ID,
                reminderNotificationBuilder.build()
            )
        }
    }

    companion object {
        const val REMINDER_NOTIFICATION_CHANNEL_ID = "REMINDER_NOTIFICATION_CHANNEL_ID"
        const val REMINDER_NOTIFICATION_ID = 1
    }
}