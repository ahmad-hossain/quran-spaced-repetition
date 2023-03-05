package com.github.ahmad_hossain.quranspacedrepetition.feature_settings.presentation.settings

import android.content.Intent

sealed class SettingsUiEvent {
    data class Toast(val s: UiText) : SettingsUiEvent()
    data class LaunchCreateDocumentIntent(val intent: Intent) : SettingsUiEvent()
    data class LaunchOpenDocumentIntent(val intent: Intent) : SettingsUiEvent()
}
