package com.example.quranspacedrepetition.feature_pages.domain.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.example.quranspacedrepetition.feature_pages.domain.use_case.UpdateReminderNotification
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

@AndroidEntryPoint
class AlarmReceiver : BroadcastReceiver() {

    @Inject
    lateinit var updateReminderNotificationUseCase: UpdateReminderNotification

    override fun onReceive(context: Context?, intent: Intent?) = goAsync {
        Timber.d("onReceive: intent=$intent")
        updateReminderNotificationUseCase()
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
}