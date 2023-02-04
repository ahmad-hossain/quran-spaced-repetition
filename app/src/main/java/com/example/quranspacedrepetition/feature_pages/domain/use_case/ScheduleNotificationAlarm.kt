package com.example.quranspacedrepetition.feature_pages.domain.use_case

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import androidx.activity.ComponentActivity
import dagger.hilt.android.qualifiers.ApplicationContext
import java.util.*
import javax.inject.Inject

class ScheduleNotificationAlarm @Inject constructor(
    @ApplicationContext private val context: Context,
    private val alarmPendingIntent: PendingIntent,
) {

    /** USE_EXACT_ALARM permission can substitute for SCHEDULE_EXACT_ALARM */
    @SuppressLint("MissingPermission")
    operator fun invoke() {
        val alarmManager = context.getSystemService(ComponentActivity.ALARM_SERVICE) as AlarmManager

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
}