package com.github.ahmad_hossain.quranspacedrepetition.feature_settings.presentation.settings

import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Mail
import androidx.compose.material.icons.filled.Upload
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.github.ahmad_hossain.quranspacedrepetition.R
import com.github.ahmad_hossain.quranspacedrepetition.feature_pages.presentation.components.CustomBottomBar
import com.github.ahmad_hossain.quranspacedrepetition.feature_pages.presentation.components.Screen
import com.github.ahmad_hossain.quranspacedrepetition.feature_settings.presentation.settings.components.EditPageRangeDialog
import com.github.ahmad_hossain.quranspacedrepetition.feature_settings.presentation.settings.components.LoadingDialog
import com.github.ahmad_hossain.quranspacedrepetition.feature_settings.presentation.settings.components.SettingsSectionHeadline
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
    val createDocumentActivityResultLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            viewModel.onEvent(SettingsEvent.OnCreateDocumentActivityResult(it))
        }
    val openDocumentActivityResultLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            viewModel.onEvent(SettingsEvent.OnOpenDocumentActivityResult(it))
        }
    val context = LocalContext.current
    LaunchedEffect(Unit) {
        viewModel.uiEvent.collect {
            when (it) {
                is SettingsUiEvent.LaunchCreateDocumentIntent -> createDocumentActivityResultLauncher.launch(it.intent)
                is SettingsUiEvent.LaunchOpenDocumentIntent -> openDocumentActivityResultLauncher.launch(it.intent)
                is SettingsUiEvent.Toast -> Toast.makeText(context, it.s.asString(context), Toast.LENGTH_SHORT).show()
            }
        }
    }
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
    LoadingDialog(
        isVisible = state.isLoadingDialogVisible,
        text = stringResource(id = R.string.applying_setting)
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
            Divider()
            SettingsSectionHeadline(text = stringResource(R.string.notifications))
            ListItem(
                modifier = Modifier.clickable { viewModel.onEvent(SettingsEvent.NotificationTimeSettingClicked) },
                headlineText = { Text(stringResource(R.string.notification_time_setting)) },
                supportingText = { Text(state.userPreferences.notificationTime.format(
                    DateTimeFormatter.ofPattern("hh:mm a"))) }
            )
            Divider()
            SettingsSectionHeadline(text = stringResource(R.string.backup))
            ListItem(
                modifier = Modifier.clickable { viewModel.onEvent(SettingsEvent.ExportDataClicked) },
                leadingContent = { Icon(Icons.Default.Upload, contentDescription = null) },
                headlineText = { Text(stringResource(R.string.headline_export_data)) },
                supportingText = { Text(stringResource(R.string.supporting_export_data)) },
            )
            ListItem(
                modifier = Modifier.clickable { viewModel.onEvent(SettingsEvent.ImportDataClicked) },
                leadingContent = { Icon(Icons.Default.Download, contentDescription = null) },
                headlineText = { Text(stringResource(R.string.headline_import_data)) },
                supportingText = { Text(stringResource(R.string.supporting_import_data)) },
            )
            Divider()
            SettingsSectionHeadline(text = "Contact")
            ListItem(
                modifier = Modifier.clickable { viewModel.onEvent(SettingsEvent.ContactClicked) },
                leadingContent = { Icon(Icons.Default.Mail, contentDescription = null) },
                headlineText = { Text(stringResource(R.string.contact_developer)) },
            )
        }
    }
}