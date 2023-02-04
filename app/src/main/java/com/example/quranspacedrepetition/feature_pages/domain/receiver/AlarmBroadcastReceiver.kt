package com.example.quranspacedrepetition.feature_pages.domain.receiver

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Context.NOTIFICATION_SERVICE
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.quranspacedrepetition.R
import com.example.quranspacedrepetition.feature_pages.domain.repository.PageRepository
import com.example.quranspacedrepetition.feature_pages.domain.use_case.ScheduleNotificationAlarm
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.first
import timber.log.Timber
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

@AndroidEntryPoint
class AlarmReceiver : BroadcastReceiver() {

    @Inject lateinit var repository: PageRepository
    @Inject lateinit var reminderNotificationBuilder: NotificationCompat.Builder
    @Inject lateinit var notificationManager: NotificationManagerCompat
    @Inject lateinit var scheduleNotificationAlarm: ScheduleNotificationAlarm

    private lateinit var context: Context

    /** Permission requested automatically for targetSDK < 33 */
    @SuppressLint("MissingPermission")
    override fun onReceive(context: Context?, intent: Intent?) = goAsync {
        Timber.d("onReceive: intent=$intent")
        this@AlarmReceiver.context = context ?: return@goAsync

        val duePages = repository.getPagesDueToday().first()
        Timber.d("onReceive: ${duePages.size} pages due today")

        withContext(Dispatchers.Main) {
            // TODO make notification dismissable if 0 pages due
            reminderNotificationBuilder.setContentText(
                context.getString(R.string.reminder_notification_text, duePages.size)
            )

            createNotificationChannel()
            notificationManager.notify(
                REMINDER_NOTIFICATION_ID,
                reminderNotificationBuilder.build()
            )

            scheduleNotificationAlarm()
        }
    }

    private fun BroadcastReceiver.goAsync(
        context: CoroutineContext = EmptyCoroutineContext,
        block: suspend CoroutineScope.() -> Unit
    ) {
        val pendingResult = goAsync()
        CoroutineScope(SupervisorJob()).launch(context) {
            try {
                block()
            } finally {
                pendingResult.finish()
            }
        }
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = context.getString(R.string.reminder_notification_channel_name)
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val mChannel =
                NotificationChannel(REMINDER_NOTIFICATION_CHANNEL_ID, name, importance)
            val notificationManager =
                context.getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(mChannel)
        }
    }

    companion object {
        const val REMINDER_NOTIFICATION_CHANNEL_ID = "REMINDER_NOTIFICATION_CHANNEL_ID"
        const val REMINDER_NOTIFICATION_ID = 1
    }
}