package com.github.ahmad_hossain.quranspacedrepetition.feature_settings.presentation.settings

import com.github.ahmad_hossain.quranspacedrepetition.feature_settings.domain.model.UserPreferences

data class SettingsState(
    val userPreferences: UserPreferences = UserPreferences(),
    val isTimePickerVisible: Boolean = false,
    val isEditPageRangeDialogVisible: Boolean = false,
    val dialogStartPage: String = "",
    val dialogEndPage: String = "",
    val dialogStartPageError: UiText? = null,
    val dialogEndPageError: UiText? = null,
)