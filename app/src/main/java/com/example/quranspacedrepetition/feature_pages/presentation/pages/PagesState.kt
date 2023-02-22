package com.example.quranspacedrepetition.feature_pages.presentation.pages

import com.example.quranspacedrepetition.feature_pages.domain.model.Page

data class PagesState constructor(
    val allPages: List<Page> = emptyList(),
    val pagesDueToday: List<Page> = emptyList(),
    val isGradeDialogVisible: Boolean = false,
    val selectedGrade: Int = 5,
    val isSearchDialogVisible: Boolean = false,
    val searchQuery: String = "",
    val searchQueryHasError: Boolean = false,
)