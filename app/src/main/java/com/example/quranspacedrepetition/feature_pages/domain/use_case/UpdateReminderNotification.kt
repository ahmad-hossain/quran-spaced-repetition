package com.example.quranspacedrepetition.feature_pages.domain.use_case

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.quranspacedrepetition.R
import com.example.quranspacedrepetition.feature_pages.domain.repository.PageRepository
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
) {

    suspend operator fun invoke() = withContext(Dispatchers.IO) {
        Timber.d("invoke()")

        val duePages = repository.getPagesDueToday().first()
        Timber.d("onReceive: ${duePages.size} pages due today")

        withContext(Dispatchers.Main) {
            // TODO make notification dismissable if 0 pages due
            reminderNotificationBuilder.setContentText(
                context.getString(R.string.reminder_notification_text, duePages.size)
            )

            createNotificationChannel()

            val notificationPermissionGranted = ActivityCompat
                .checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED
            if (notificationPermissionGranted) {
                notificationManager.notify(
                    REMINDER_NOTIFICATION_ID,
                    reminderNotificationBuilder.build()
                )
            }

            scheduleNotificationAlarm()
        }
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = context.getString(R.string.reminder_notification_channel_name)
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val mChannel =
                NotificationChannel(REMINDER_NOTIFICATION_CHANNEL_ID, name, importance)
            val notificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(mChannel)
        }
    }

    companion object {
        const val REMINDER_NOTIFICATION_CHANNEL_ID = "REMINDER_NOTIFICATION_CHANNEL_ID"
        const val REMINDER_NOTIFICATION_ID = 1
    }
}