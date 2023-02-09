package com.example.quranspacedrepetition.feature_pages.presentation.pages

import com.example.quranspacedrepetition.feature_pages.domain.model.Page

sealed class PagesEvent {
    data class PageClicked(val page: Page) : PagesEvent()
    object TodayChipClicked : PagesEvent()
    object AllChipClicked : PagesEvent()
    object GradeDialogConfirmed : PagesEvent()
    object GradeDialogDismissed : PagesEvent()
    object SearchFabClicked : PagesEvent()
    object SearchDialogConfirmed : PagesEvent()
    object SearchDialogDismissed : PagesEvent()
    data class SearchQueryChanged(val query: String) : PagesEvent()
    data class NumberPickerValueChanged(val newValue: Int) : PagesEvent()
    data class GradeSelected(val grade: Int) : PagesEvent()
}