package com.github.ahmad_hossain.quranspacedrepetition.feature_pages.presentation.pages

import com.github.ahmad_hossain.quranspacedrepetition.feature_settings.presentation.settings.UiText
import comgithubahmadhossainquranspacedrepetition.Page

data class PagesState(
    val displayedPages: List<Page> = emptyList(),
    val allPages: List<Page> = emptyList(),
    val pagesDueToday: List<Page> = emptyList(),
    val selectedTab: UiTabs = UiTabs.TODAY,
    val isGradeDialogVisible: Boolean = false,
    val lastClickedPageNumber: Int = -1,
    val selectedGrade: Int = 5,
    val isSearchDialogVisible: Boolean = false,
    val searchQuery: String = "",
    val searchQueryError: UiText? = null,
)