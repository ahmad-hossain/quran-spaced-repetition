package com.example.quranspacedrepetition.feature_settings.presentation.settings

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.datastore.core.DataStore
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.quranspacedrepetition.feature_settings.domain.model.UserPreferences
import com.example.quranspacedrepetition.feature_settings.domain.repository.SettingsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val settingsRepository: SettingsRepository,
    dataStore: DataStore<UserPreferences>,
) : ViewModel() {

    var state by mutableStateOf(SettingsState())
        private set

    fun onEvent(event: SettingsEvent) {
        Timber.d("%s : %s", event::class.simpleName, event.toString())

        when (event) {
            is SettingsEvent.NotificationTimeSettingClicked -> state = state.copy(isTimePickerVisible = true)
            is SettingsEvent.PageNumberSettingClicked -> TODO()
            is SettingsEvent.TimePickerTimeChanged -> {
                state = state.copy(isTimePickerVisible = false)
                viewModelScope.launch {
                    settingsRepository.updateDatastore(state.userPreferences.copy(notificationTime = event.time))
                }
                // todo : update alarm manager
            }
            is SettingsEvent.TimePickerDismissed -> state = state.copy(isTimePickerVisible = false)
        }
    }

    init {
        dataStore.data.onEach {
            state = state.copy(userPreferences = it)
        }.launchIn(viewModelScope)
    }
}