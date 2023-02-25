package com.example.quranspacedrepetition.feature_settings.presentation.settings

import java.time.LocalTime

sealed class SettingsEvent {
    object NotificationTimeSettingClicked : SettingsEvent()
    object PageNumberSettingClicked : SettingsEvent()
    object TimePickerDismissed : SettingsEvent()
    data class TimePickerTimeChanged(val time: LocalTime) : SettingsEvent()
    data class EditPageRangeDialogStartPageChanged(val startPage: String) : SettingsEvent()
    data class EditPageRangeDialogEndPageChanged(val endPage: String) : SettingsEvent()
    object EditPageRangeDialogConfirmed : SettingsEvent()
    object EditPageRangeDialogDismissed : SettingsEvent()
}