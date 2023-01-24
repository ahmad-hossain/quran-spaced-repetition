package com.example.quranspacedrepetition

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.quranspacedrepetition.feature_pages.presentation.pages.NavGraphs
import com.example.quranspacedrepetition.ui.theme.QuranSpacedRepetitionTheme
import com.ramcosta.composedestinations.DestinationsNavHost
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            QuranSpacedRepetitionTheme {
                DestinationsNavHost(navGraph = NavGraphs.root)
            }
        }
    }
}