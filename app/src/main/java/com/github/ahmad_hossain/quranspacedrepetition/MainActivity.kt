package com.github.ahmad_hossain.quranspacedrepetition

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.provider.Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import com.github.ahmad_hossain.quranspacedrepetition.common.use_case.ScheduleNotificationAlarm
import com.github.ahmad_hossain.quranspacedrepetition.feature_pages.domain.repository.PageRepository
import com.github.ahmad_hossain.quranspacedrepetition.feature_pages.domain.use_case.UpdateReminderNotification
import com.github.ahmad_hossain.quranspacedrepetition.feature_pages.presentation.components.PermissionDialog
import com.github.ahmad_hossain.quranspacedrepetition.feature_pages.presentation.pages.PagesViewModel
import com.github.ahmad_hossain.quranspacedrepetition.feature_settings.domain.repository.SettingsRepository
import com.github.ahmad_hossain.quranspacedrepetition.feature_settings.presentation.settings.SettingsViewModel
import com.github.ahmad_hossain.quranspacedrepetition.ui.theme.QuranSpacedRepetitionTheme
import com.ramcosta.composedestinations.DestinationsNavHost
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject lateinit var scheduleNotificationAlarm: ScheduleNotificationAlarm
    private val viewModel by viewModels<PagesViewModel>()
    @Inject lateinit var notificationManager: NotificationManagerCompat
    @Inject lateinit var settingsRepo: SettingsRepository
    @Inject lateinit var pageRepository: PageRepository
    @Inject lateinit var alarmManager: AlarmManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen().setKeepOnScreenCondition { viewModel.state.allPages.isEmpty() }

        requestPermissions()
        createNotificationChannel()
        scheduleNotificationAlarm()

        if (intent.action == SettingsViewModel.INTENT_ACTION_RESTART) {
            updatePageRangePref()
        }

        val prefs = getSharedPreferences("prefs", MODE_PRIVATE)
        setContent {
            QuranSpacedRepetitionTheme {
                Surface {
                    if (Build.VERSION.SDK_INT > Build.VERSION_CODES.TIRAMISU) {
                        AlarmPermissionDialog(prefs)
                    }
                    DestinationsNavHost(navGraph = NavGraphs.root)
                }
            }
        }
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = getString(R.string.reminder_notification_channel_name)
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val mChannel =
                NotificationChannel(UpdateReminderNotification.REMINDER_NOTIFICATION_CHANNEL_ID, name, importance)
            notificationManager.createNotificationChannel(mChannel)
        }
    }

    private fun requestPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                1
            )
        }
    }

    private fun updatePageRangePref() {
        lifecycleScope.launch(Dispatchers.IO) {
            val pages = pageRepository.getPages().first()
            val minPage = async { pages.minOf { it.pageNumber } }
            val maxPage = async { pages.maxOf { it.pageNumber } }
            settingsRepo.updateDatastore {
                it.copy(
                    startPage = minPage.await(),
                    endPage = maxPage.await()
                )
            }
        }
    }

    /** API check handled */
    @SuppressLint("InlinedApi")
    @Composable
    private fun AlarmPermissionDialog(prefs: SharedPreferences) {
        val isAlarmDialogVisible = rememberSaveable {
            mutableStateOf(prefs.getBoolean("showAlarmDialog", true))
        }

        fun hideAndDisableAlarmDialog() {
            isAlarmDialogVisible.value = false
            prefs.edit().putBoolean("showAlarmDialog", false).apply()
        }

        if (isAlarmDialogVisible.value) {
            PermissionDialog(
                icon = painterResource(id = R.drawable.ic_alarm_filled),
                permission = getString(R.string.schedule_alarms),
                text = getString(R.string.app_name) + " " + getString(R.string.alarm_permission_dialog_text),
                confirmButtonText = stringResource(R.string.go_to_settings),
                dismissButtonText = stringResource(id = R.string.cancel),
                onConfirmClicked = {
                    startActivity(Intent(ACTION_REQUEST_SCHEDULE_EXACT_ALARM))
                    hideAndDisableAlarmDialog()
                },
                onDismissed = ::hideAndDisableAlarmDialog
            )
        }
    }
}