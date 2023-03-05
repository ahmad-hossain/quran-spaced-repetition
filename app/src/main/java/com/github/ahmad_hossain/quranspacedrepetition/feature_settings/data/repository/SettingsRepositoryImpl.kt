package com.github.ahmad_hossain.quranspacedrepetition.feature_settings.data.repository

import androidx.datastore.core.DataStore
import com.github.ahmad_hossain.quranspacedrepetition.feature_settings.domain.model.UserPreferences
import com.github.ahmad_hossain.quranspacedrepetition.feature_settings.domain.repository.SettingsRepository
import javax.inject.Inject

class SettingsRepositoryImpl @Inject constructor(
    private val dataStore: DataStore<UserPreferences>
) : SettingsRepository {

    override suspend fun updateDatastore(pref: UserPreferences) {
        dataStore.updateData { pref }
    }
}
