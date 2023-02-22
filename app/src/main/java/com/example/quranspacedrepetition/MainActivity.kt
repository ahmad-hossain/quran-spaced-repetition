package com.example.quranspacedrepetition

import android.Manifest
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.core.app.ActivityCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.example.quranspacedrepetition.feature_pages.domain.use_case.ScheduleNotificationAlarm
import com.example.quranspacedrepetition.feature_pages.presentation.pages.PagesViewModel
import com.example.quranspacedrepetition.ui.theme.QuranSpacedRepetitionTheme
import com.ramcosta.composedestinations.DestinationsNavHost
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject lateinit var scheduleNotificationAlarm: ScheduleNotificationAlarm
    private val viewModel by viewModels<PagesViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen().setKeepOnScreenCondition { viewModel.state.allPages.isEmpty() }

        requestPermissions()
        scheduleNotificationAlarm()

        setContent {
            QuranSpacedRepetitionTheme {
                DestinationsNavHost(navGraph = NavGraphs.root)
            }
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
}