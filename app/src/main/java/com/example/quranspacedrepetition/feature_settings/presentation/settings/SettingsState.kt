package com.example.quranspacedrepetition.feature_settings.presentation.settings

import com.example.quranspacedrepetition.feature_settings.domain.model.UserPreferences

data class SettingsState(
    val userPreferences: UserPreferences = UserPreferences()
)
