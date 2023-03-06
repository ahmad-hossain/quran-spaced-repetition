package com.github.ahmad_hossain.quranspacedrepetition.feature_settings.presentation.settings

import android.app.Activity
import android.app.Application
import android.content.Intent
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.datastore.core.DataStore
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.ahmad_hossain.quranspacedrepetition.R
import com.github.ahmad_hossain.quranspacedrepetition.common.use_case.ScheduleNotificationAlarm
import com.github.ahmad_hossain.quranspacedrepetition.feature_pages.data.data_source.PageDatabase
import com.github.ahmad_hossain.quranspacedrepetition.feature_pages.domain.repository.PageRepository
import com.github.ahmad_hossain.quranspacedrepetition.feature_settings.domain.model.UserPreferences
import com.github.ahmad_hossain.quranspacedrepetition.feature_settings.domain.repository.SettingsRepository
import com.github.ahmad_hossain.quranspacedrepetition.feature_settings.domain.use_case.ChangePageRange
import com.github.ahmad_hossain.quranspacedrepetition.feature_settings.domain.use_case.ValidSqlLiteDb
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject
import kotlin.system.exitProcess

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val settingsRepository: SettingsRepository,
    private val dataStore: DataStore<UserPreferences>,
    private val scheduleNotificationAlarmUseCase: ScheduleNotificationAlarm,
    private val app: Application,
    private val validSqlLiteDbUseCase: ValidSqlLiteDb,
    private val db: PageDatabase,
    private val pageRepository: PageRepository,
    private val changePageRangeUseCase: ChangePageRange,
) : ViewModel() {

    var state by mutableStateOf(SettingsState())
        private set
    private val _uiEvent = MutableSharedFlow<SettingsUiEvent>()
    val uiEvent: SharedFlow<SettingsUiEvent> = _uiEvent.asSharedFlow()

    fun onEvent(event: SettingsEvent) {
        Timber.d("%s : %s", event::class.simpleName, event.toString())

        when (event) {
            is SettingsEvent.NotificationTimeSettingClicked -> state = state.copy(isTimePickerVisible = true)
            is SettingsEvent.PageNumberSettingClicked -> resetEditPageRangeDialog(isVisible = true)
            is SettingsEvent.TimePickerTimeChanged -> {
                state = state.copy(isTimePickerVisible = false)
                viewModelScope.launch {
                    settingsRepository.updateDatastore(state.userPreferences.copy(notificationTime = event.time))
                    scheduleNotificationAlarmUseCase()
                }
            }
            is SettingsEvent.TimePickerDismissed -> state = state.copy(isTimePickerVisible = false)
            is SettingsEvent.EditPageRangeDialogConfirmed -> {
                val newPageRange = state.dialogStartPage.toInt()..state.dialogEndPage.toInt()
                resetEditPageRangeDialog()
                viewModelScope.launch(Dispatchers.IO) {
                    changePageRangeUseCase(newPageRange)
                    dataStore.updateData {
                        it.copy(startPage = newPageRange.first, endPage = newPageRange.last)
                    }
                }
            }
            is SettingsEvent.EditPageRangeDialogDismissed -> resetEditPageRangeDialog()
            is SettingsEvent.EditPageRangeDialogStartPageChanged -> {
                val start = event.startPage
                val end = state.dialogEndPage
                val startInt = start.toIntOrNull()
                val startPageError = when {
                    start.isEmpty() -> UiText.StringResource(R.string.empty_field_error)
                    startInt == null -> {
                        state = state.copy(
                            dialogStartPage = state.dialogStartPage,
                            dialogStartPageError = UiText.StringResource(R.string.invalid_number)
                        )
                        return
                    }
                    startInt >= end.toInt() -> UiText.StringResource(R.string.start_greater_than_end_error)
                    else -> null
                }
                state = state.copy(dialogStartPage = event.startPage, dialogStartPageError = startPageError)
            }
            is SettingsEvent.EditPageRangeDialogEndPageChanged -> {
                val start = state.dialogStartPage
                val end = event.endPage
                val endInt = end.toIntOrNull()
                val endPageError = when {
                    end.isEmpty() -> UiText.StringResource(R.string.empty_field_error)
                    endInt == null -> {
                        state = state.copy(
                            dialogEndPage = state.dialogEndPage,
                            dialogEndPageError = UiText.StringResource(R.string.invalid_number)
                        )
                        return
                    }
                    end.toInt() <= start.toInt() -> UiText.StringResource(R.string.end_less_than_start_error)
                    else -> null
                }
                state = state.copy(dialogEndPage = event.endPage, dialogEndPageError = endPageError)
            }
            is SettingsEvent.ExportDataClicked -> {
                val intent = Intent(Intent.ACTION_CREATE_DOCUMENT).apply {
                    type = "*/*"
                    putExtra(Intent.EXTRA_TITLE, "quran_hifz_revision_backup.db")
                }
                viewModelScope.launch {
                    _uiEvent.emit(SettingsUiEvent.LaunchCreateDocumentIntent(intent))
                }
            }
            is SettingsEvent.ImportDataClicked -> {
                val dbMimeTypes = arrayOf("application/vnd.sqlite3", "application/octet-stream")
                val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
                    addCategory(Intent.CATEGORY_OPENABLE)
                    type = "*/*"
                    putExtra(Intent.EXTRA_MIME_TYPES, dbMimeTypes)
                }
                viewModelScope.launch {
                    _uiEvent.emit(SettingsUiEvent.LaunchOpenDocumentIntent(intent))
                }
            }
            is SettingsEvent.OnCreateDocumentActivityResult -> {
                if (event.result.resultCode != Activity.RESULT_OK) return
                val userChosenUri = event.result.data?.data
                if (userChosenUri == null) {
                    viewModelScope.launch { _uiEvent.emit(SettingsUiEvent.Toast(UiText.StringResource(R.string.error_null_uri))) }
                    return
                }

                viewModelScope.launch(Dispatchers.IO) {
                    pageRepository.checkpoint()

                    val inputStream = app.getDatabasePath(PageDatabase.DATABASE_NAME).inputStream()
                    val outputStream = app.contentResolver.openOutputStream(userChosenUri) ?: return@launch

                    inputStream.copyTo(outputStream)

                    inputStream.close()
                    outputStream.close()
                }
            }
            is SettingsEvent.OnOpenDocumentActivityResult -> {
                if (event.result.resultCode != Activity.RESULT_OK) return
                val dbUri = event.result.data?.data ?: return
                if (!validSqlLiteDbUseCase.isValid(dbUri)) {
                    Timber.d("OnOpenDocumentActivityResult: Invalid sqlite db")
                    viewModelScope.launch { _uiEvent.emit(SettingsUiEvent.Toast(UiText.StringResource(R.string.error_invalid_file))) }
                    return
                }

                viewModelScope.launch(Dispatchers.IO) {
                    db.close()

                    val inputStream = app.contentResolver.openInputStream(dbUri) ?: return@launch
                    val outputStream = app.getDatabasePath(PageDatabase.DATABASE_NAME).outputStream()

                    inputStream.copyTo(outputStream)
                    inputStream.close()
                    outputStream.close()

                    val intent = app.packageManager.getLaunchIntentForPackage(app.packageName)
                    intent?.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    app.startActivity(intent)
                    exitProcess(0)
                }
            }
        }
    }

    private fun resetEditPageRangeDialog(isVisible: Boolean = false) {
        viewModelScope.launch {
            val prefs = dataStore.data.first()
            state = state.copy(
                isEditPageRangeDialogVisible = isVisible,
                dialogStartPage = prefs.startPage.toString(),
                dialogEndPage = prefs.endPage.toString(),
            )
        }
    }

    init {
        dataStore.data.onEach {
            state = state.copy(userPreferences = it)
        }.launchIn(viewModelScope)
    }
}