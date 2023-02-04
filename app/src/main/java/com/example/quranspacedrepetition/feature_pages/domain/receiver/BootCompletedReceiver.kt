package com.example.quranspacedrepetition.feature_pages.domain.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.example.quranspacedrepetition.feature_pages.domain.use_case.ScheduleNotificationAlarm
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class BootCompletedReceiver : BroadcastReceiver() {

    @Inject
    lateinit var scheduleNotificationAlarm: ScheduleNotificationAlarm

    override fun onReceive(context: Context?, intent: Intent?) {
        Timber.d("onReceive: intent=$intent")
        if (intent?.action != Intent.ACTION_BOOT_COMPLETED) return
        scheduleNotificationAlarm()
    }
}