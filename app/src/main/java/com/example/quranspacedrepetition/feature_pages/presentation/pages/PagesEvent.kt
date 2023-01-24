package com.example.quranspacedrepetition.feature_pages.presentation.pages

import com.example.quranspacedrepetition.feature_pages.domain.model.Page

sealed class PagesEvent {
    data class PageClicked(val page: Page) : PagesEvent()
}