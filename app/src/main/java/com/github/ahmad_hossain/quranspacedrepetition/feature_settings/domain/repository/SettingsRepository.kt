package com.github.ahmad_hossain.quranspacedrepetition.feature_settings.domain.repository

import com.github.ahmad_hossain.quranspacedrepetition.feature_settings.domain.model.UserPreferences
import kotlinx.coroutines.flow.Flow

interface SettingsRepository {

    fun getDatastoreData(): Flow<UserPreferences>

    suspend fun updateDatastore(transform: suspend (t: UserPreferences) -> UserPreferences)
}