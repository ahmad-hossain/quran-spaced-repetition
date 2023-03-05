package com.github.ahmad_hossain.quranspacedrepetition.feature_pages.presentation.pages

import com.github.ahmad_hossain.quranspacedrepetition.feature_pages.domain.model.Page
import com.github.ahmad_hossain.quranspacedrepetition.feature_settings.presentation.settings.UiText

data class PagesState(
    val displayedPages: List<Page> = emptyList(),
    val allPages: List<Page> = emptyList(),
    val pagesDueToday: List<Page> = emptyList(),
    val selectedTab: UiTabs = UiTabs.TODAY,
    val isGradeDialogVisible: Boolean = false,
    val selectedGrade: Int = 5,
    val isSearchDialogVisible: Boolean = false,
    val searchQuery: String = "",
    val searchQueryError: UiText? = null,
)