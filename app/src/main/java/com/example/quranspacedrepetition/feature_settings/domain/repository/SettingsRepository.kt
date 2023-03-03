package com.example.quranspacedrepetition.feature_settings.domain.repository

import com.example.quranspacedrepetition.feature_settings.domain.model.UserPreferences

interface SettingsRepository {

    suspend fun updateDatastore(pref: UserPreferences)
}