package com.github.ahmad_hossain.quranspacedrepetition.feature_settings.domain.repository

import com.github.ahmad_hossain.quranspacedrepetition.feature_settings.domain.model.UserPreferences

interface SettingsRepository {

    suspend fun updateDatastore(pref: UserPreferences)
}