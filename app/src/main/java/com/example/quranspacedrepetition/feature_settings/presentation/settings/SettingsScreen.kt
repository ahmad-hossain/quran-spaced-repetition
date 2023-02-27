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
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.quranspacedrepetition.R
import com.example.quranspacedrepetition.feature_pages.presentation.components.CustomBottomBar
import com.example.quranspacedrepetition.feature_pages.presentation.components.Screen
import com.example.quranspacedrepetition.feature_settings.presentation.settings.components.EditPageRangeDialog
import com.example.quranspacedrepetition.feature_settings.presentation.settings.components.SettingsSectionHeadline
import com.marosseleng.compose.material3.datetimepickers.time.ui.dialog.TimePickerDialog
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

    if (state.isTimePickerVisible) {
        TimePickerDialog(
            title = { Text(stringResource(R.string.select_time)) },
            initialTime = state.userPreferences.notificationTime,
            onDismissRequest = { viewModel.onEvent(SettingsEvent.TimePickerDismissed) },
            onTimeChange = { viewModel.onEvent(SettingsEvent.TimePickerTimeChanged(it)) }
        )
    }
    EditPageRangeDialog(
        isVisible = state.isEditPageRangeDialogVisible,
        startPage = state.dialogStartPage,
        endPage = state.dialogEndPage,
        startPageError = state.dialogStartPageError,
        endPageError = state.dialogEndPageError,
        onStartPageChanged = { viewModel.onEvent(SettingsEvent.EditPageRangeDialogStartPageChanged(it)) },
        onEndPageChanged = { viewModel.onEvent(SettingsEvent.EditPageRangeDialogEndPageChanged(it)) },
        onDismissRequest = { viewModel.onEvent(SettingsEvent.EditPageRangeDialogDismissed) },
        onConfirmClicked = { viewModel.onEvent(SettingsEvent.EditPageRangeDialogConfirmed) }
    )

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
            SettingsSectionHeadline(text = stringResource(R.string.revision))
            ListItem(
                modifier = Modifier.clickable { viewModel.onEvent(SettingsEvent.PageNumberSettingClicked) },
                headlineText = { Text(stringResource(R.string.quran_pages_setting)) },
                supportingText = { Text("${state.userPreferences.startPage} - ${state.userPreferences.endPage}") }
            )
            Divider(modifier = Modifier.padding(vertical = 16.dp))
            SettingsSectionHeadline(text = stringResource(R.string.notifications))
            ListItem(
                modifier = Modifier.clickable { viewModel.onEvent(SettingsEvent.NotificationTimeSettingClicked) },
                headlineText = { Text(stringResource(R.string.notification_time_setting)) },
                supportingText = { Text(state.userPreferences.notificationTime.format(
                    DateTimeFormatter.ofPattern("hh:mm a"))) }
            )
        }
    }
}