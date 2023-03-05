package com.github.ahmad_hossain.quranspacedrepetition.feature_pages.presentation.pages

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.ahmad_hossain.quranspacedrepetition.R
import com.github.ahmad_hossain.quranspacedrepetition.feature_pages.domain.model.Page
import com.github.ahmad_hossain.quranspacedrepetition.feature_pages.domain.repository.PageRepository
import com.github.ahmad_hossain.quranspacedrepetition.feature_pages.domain.use_case.SuperMemo
import com.github.ahmad_hossain.quranspacedrepetition.feature_pages.domain.use_case.UpdateReminderNotification
import com.github.ahmad_hossain.quranspacedrepetition.feature_pages.presentation.pages.PagesEvent.*
import com.github.ahmad_hossain.quranspacedrepetition.feature_settings.presentation.settings.UiText
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import timber.log.Timber
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class PagesViewModel @Inject constructor(
    private val repository: PageRepository,
    private val superMemoUseCase: SuperMemo,
    private val updateReminderNotificationUseCase: UpdateReminderNotification,
) : ViewModel() {

    var state by mutableStateOf(PagesState())
        private set
    private lateinit var lastClickedPage: Page

    private val _scrollToIndex = MutableSharedFlow<Int>()
    val scrollToIndex = _scrollToIndex.asSharedFlow()

    fun onEvent(event: PagesEvent) {
        Timber.d("%s : %s", event::class.simpleName, event.toString())

        when (event) {
            is PageClicked -> {
                state = state.copy(isGradeDialogVisible = true)
                lastClickedPage = event.page
            }
            is TabClicked -> {
                when (event.tab) {
                    UiTabs.TODAY -> state = state.copy(
                        selectedTab = event.tab,
                        displayedPages = state.pagesDueToday
                    )
                    UiTabs.ALL -> state = state.copy(
                        selectedTab = event.tab,
                        displayedPages = state.allPages
                    )
                }
            }
            is GradeDialogDismissed -> state = state.copy(isGradeDialogVisible = false, selectedGrade = 5)
            is GradeDialogConfirmed -> {
                val grade = state.selectedGrade
                state = state.copy(isGradeDialogVisible = false, selectedGrade = 5)

                val updatedPage = superMemoUseCase(page = lastClickedPage, grade = grade)
                viewModelScope.launch(Dispatchers.IO) {
                    repository.updatePage(
                        updatedPage.copy(
                            dueDate = LocalDate.now().plusDays(updatedPage.interval.toLong())
                        )
                    )
                    if (state.pagesDueToday.contains(lastClickedPage))
                        updateReminderNotificationUseCase()
                }
            }
            is NumberPickerValueChanged -> state = state.copy(selectedGrade = event.newValue)
            is GradeSelected -> state = state.copy(selectedGrade = event.grade)
            is SearchFabClicked -> state = state.copy(isSearchDialogVisible = true)
            is SearchDialogConfirmed -> {
                val queriedPageNum = state.searchQuery.toInt()
                resetSearchDialog()
                viewModelScope.launch {
                    val queriedPageIndex = state.displayedPages
                        .indexOfFirst { it.pageNumber == queriedPageNum }
                        .takeUnless { it == -1 }
                        ?: queriedPageNum.coerceIn(0, state.displayedPages.lastIndex)
                    _scrollToIndex.emit(queriedPageIndex)
                }
            }
            is SearchDialogDismissed -> resetSearchDialog()
            is SearchQueryChanged -> {
                val searchQueryError = when {
                    event.query.isEmpty() -> UiText.StringResource(R.string.empty_field_error)
                    event.query.toIntOrNull() == null -> {
                        state = state.copy(
                            searchQuery = state.searchQuery,
                            searchQueryError = UiText.StringResource(R.string.invalid_number)
                        )
                        return
                    }
                    else -> null
                }
                state = state.copy(searchQuery = event.query, searchQueryError = searchQueryError)
            }
        }
    }

    private fun resetSearchDialog() {
        state = state.copy(
            isSearchDialogVisible = false,
            searchQuery = "",
            searchQueryError = null
        )
    }

    init {
        repository.getPagesDueToday().onEach {
            state = state.copy(
                displayedPages = if (state.selectedTab == UiTabs.TODAY) it else state.displayedPages,
                pagesDueToday = it
            )
        }.launchIn(viewModelScope)

        repository.getPages().onEach {
            state = state.copy(
                displayedPages = if (state.selectedTab == UiTabs.ALL) it else state.displayedPages,
                allPages = it
            )
        }.launchIn(viewModelScope)
    }
}