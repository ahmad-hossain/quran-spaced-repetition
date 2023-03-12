package com.github.ahmad_hossain.quranspacedrepetition.feature_pages.presentation.pages

sealed class UiEvent {
    data class ScrollToIndex(val index: Int) : UiEvent()
    object ExpandTopAndBottomBars : UiEvent()
}