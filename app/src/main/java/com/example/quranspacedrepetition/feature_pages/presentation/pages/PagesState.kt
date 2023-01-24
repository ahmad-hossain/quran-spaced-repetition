package com.example.quranspacedrepetition.feature_pages.presentation.pages

import com.example.quranspacedrepetition.feature_pages.domain.model.Page

data class PagesState(
    val allPages: List<Page> = emptyList(),
    val pagesDueToday: List<Page> = emptyList(),
    val isTodayChipSelected: Boolean = true,
    val isAllChipSelected: Boolean = false,
)
