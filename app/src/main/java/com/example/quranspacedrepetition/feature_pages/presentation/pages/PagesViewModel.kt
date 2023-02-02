package com.example.quranspacedrepetition.feature_pages.presentation.pages

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.quranspacedrepetition.feature_pages.domain.model.Page
import com.example.quranspacedrepetition.feature_pages.domain.repository.PageRepository
import com.example.quranspacedrepetition.feature_pages.domain.use_case.SuperMemo
import com.example.quranspacedrepetition.feature_pages.presentation.pages.PagesEvent.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
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
) : ViewModel() {

    var state by mutableStateOf(PagesState())
        private set
    private lateinit var lastClickedPage: Page

    fun onEvent(event: PagesEvent) {
        Timber.d("%s : %s", event::class.simpleName, event.toString())

        when (event) {
            is PageClicked -> {
                state = state.copy(isGradeDialogVisible = true)
                lastClickedPage = event.page
            }
            is TodayChipClicked -> {
                state = state.copy(
                    displayedPages = state.pagesDueToday,
                    isTodayChipSelected = true,
                    isAllChipSelected = false
                )
            }
            is AllChipClicked -> {
                state = state.copy(
                    displayedPages = state.allPages,
                    isTodayChipSelected = false,
                    isAllChipSelected = true
                )
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
                }
            }
            is NumberPickerValueChanged -> state = state.copy(selectedGrade = event.newValue)
            is GradeSelected -> state = state.copy(selectedGrade = event.grade)
        }
    }

    init {
        repository.getPagesDueToday().onEach {
            state = state.copy(
                displayedPages = if (state.isTodayChipSelected) it else state.displayedPages,
                pagesDueToday = it
            )
        }.launchIn(viewModelScope)

        repository.getPages().onEach {
            state = state.copy(
                displayedPages = if (state.isAllChipSelected) it else state.displayedPages,
                allPages = it
            )
        }.launchIn(viewModelScope)
    }
}
