package com.github.ahmad_hossain.quranspacedrepetition

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material3.Surface
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import com.github.ahmad_hossain.quranspacedrepetition.common.use_case.ScheduleNotificationAlarm
import com.github.ahmad_hossain.quranspacedrepetition.feature_pages.domain.repository.PageRepository
import com.github.ahmad_hossain.quranspacedrepetition.feature_pages.domain.use_case.UpdateReminderNotification
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen().setKeepOnScreenCondition { viewModel.state.allPages.isEmpty() }

        requestPermissions()
        createNotificationChannel()
        scheduleNotificationAlarm()

        if (intent.action == SettingsViewModel.INTENT_ACTION_RESTART) {
            updatePageRangePref()
        }

        setContent {
            QuranSpacedRepetitionTheme {
                Surface {
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
            val data = settingsRepo.getDatastoreData().first()
            val pages = pageRepository.getPages().first()
            val minPage = async { pages.minOf { it.pageNumber } }
            val maxPage = async { pages.maxOf { it.pageNumber } }
            settingsRepo.updateDatastore {
                data.copy(
                    startPage = minPage.await(),
                    endPage = maxPage.await()
                )
            }
        }
    }
}