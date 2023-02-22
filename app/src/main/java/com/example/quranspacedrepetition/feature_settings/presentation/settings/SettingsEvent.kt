package com.example.quranspacedrepetition.feature_settings.presentation.settings

sealed class SettingsEvent {
    object NotificationTimeSettingClicked : SettingsEvent()
    object PageNumberSettingClicked : SettingsEvent()
}