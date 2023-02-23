package com.example.quranspacedrepetition.feature_settings.presentation.settings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.quranspacedrepetition.R
import com.example.quranspacedrepetition.feature_pages.presentation.components.CustomBottomBar
import com.example.quranspacedrepetition.feature_pages.presentation.components.Screen
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Destination
@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel = hiltViewModel(),
    navigator: DestinationsNavigator,
) {
    val state = viewModel.state

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.settings)) },
                navigationIcon = {
                    IconButton(onClick = { navigator.popBackStack() }) {
                        Icon(
                            Icons.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.navigate_back)
                        )
                    }
                }
            )
        },
        bottomBar = {
            CustomBottomBar(
                currentScreen = Screen.Settings,
                onHomeClicked = { navigator.popBackStack() },
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            ListItem(
                modifier = Modifier.clickable { viewModel.onEvent(SettingsEvent.NotificationTimeSettingClicked) },
                headlineText = { Text(stringResource(R.string.notification_time_setting)) },
                supportingText = { Text(state.userPreferences.notificationTime.format(
                    DateTimeFormatter.ofPattern("hh:mm a"))) }
            )
            ListItem(
                modifier = Modifier.clickable { viewModel.onEvent(SettingsEvent.NotificationTimeSettingClicked) },
                headlineText = { Text(stringResource(R.string.quran_pages_setting)) },
                supportingText = { Text("${state.userPreferences.startPage} - ${state.userPreferences.endPage}") }
            )
        }
    }
}