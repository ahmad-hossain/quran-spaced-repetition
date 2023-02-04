package com.example.quranspacedrepetition

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.app.ActivityCompat
import com.example.quranspacedrepetition.feature_pages.domain.use_case.ScheduleNotificationAlarm
import com.example.quranspacedrepetition.feature_pages.presentation.pages.NavGraphs
import com.example.quranspacedrepetition.ui.theme.QuranSpacedRepetitionTheme
import com.ramcosta.composedestinations.DestinationsNavHost
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject lateinit var scheduleNotificationAlarm: ScheduleNotificationAlarm

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

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