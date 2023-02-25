package com.example.quranspacedrepetition.feature_settings.presentation.settings

import android.content.Context
import android.content.Intent
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.datastore.core.DataStore
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.quranspacedrepetition.R
import com.example.quranspacedrepetition.common.use_case.ScheduleNotificationAlarm
import com.example.quranspacedrepetition.feature_pages.data.data_source.PageDatabase
import com.example.quranspacedrepetition.feature_settings.domain.model.UiText
import com.example.quranspacedrepetition.feature_settings.domain.model.UserPreferences
import com.example.quranspacedrepetition.feature_settings.domain.repository.SettingsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject
import kotlin.system.exitProcess

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val settingsRepository: SettingsRepository,
    private val dataStore: DataStore<UserPreferences>,
    private val scheduleNotificationAlarmUseCase: ScheduleNotificationAlarm,
    @ApplicationContext private val appContext: Context,
) : ViewModel() {

    var state by mutableStateOf(SettingsState())
        private set

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
                val startPage = state.dialogStartPage.toInt()
                val endPage = state.dialogEndPage.toInt()
                resetEditPageRangeDialog()
                viewModelScope.launch(Dispatchers.IO) {
                    dataStore.updateData { it.copy(startPage = startPage, endPage = endPage) }

                    appContext.deleteDatabase(PageDatabase.DATABASE_NAME)

                    val intent = appContext.packageManager.getLaunchIntentForPackage(appContext.packageName)
                    intent?.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    appContext.startActivity(intent)
                    exitProcess(0)
                }
            }
            is SettingsEvent.EditPageRangeDialogDismissed -> resetEditPageRangeDialog()
            is SettingsEvent.EditPageRangeDialogStartPageChanged -> {
                val start = event.startPage
                val end = state.dialogEndPage
                val startPageError = when {
                    start.isEmpty() -> UiText.StringResource(R.string.empty_field_error)
                    start.toInt() >= end.toInt() -> UiText.StringResource(R.string.start_greater_than_end_error)
                    else -> null
                }
                state = state.copy(dialogStartPage = event.startPage, dialogStartPageError = startPageError)
            }
            is SettingsEvent.EditPageRangeDialogEndPageChanged -> {
                val start = state.dialogStartPage
                val end = event.endPage
                val endPageError = when {
                    end.isEmpty() -> UiText.StringResource(R.string.empty_field_error)
                    end.toInt() <= start.toInt() -> UiText.StringResource(R.string.end_less_than_start_error)
                    else -> null
                }
                state = state.copy(dialogEndPage = event.endPage, dialogEndPageError = endPageError)
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