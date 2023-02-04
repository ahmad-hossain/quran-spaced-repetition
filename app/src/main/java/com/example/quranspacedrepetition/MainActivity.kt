package com.example.quranspacedrepetition

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.quranspacedrepetition.feature_pages.domain.use_case.ScheduleNotificationAlarm
import com.example.quranspacedrepetition.feature_pages.presentation.pages.NavGraphs
import com.example.quranspacedrepetition.ui.theme.QuranSpacedRepetitionTheme
import com.ramcosta.composedestinations.DestinationsNavHost
import dagger.hilt.android.AndroidEntryPoint
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject lateinit var scheduleNotificationAlarm: ScheduleNotificationAlarm

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        scheduleNotificationAlarm()

        setContent {
            QuranSpacedRepetitionTheme {
                DestinationsNavHost(navGraph = NavGraphs.root)
            }
        }
    }
}