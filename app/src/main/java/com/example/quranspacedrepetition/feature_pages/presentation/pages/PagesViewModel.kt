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
    lateinit var lastClickedPage: Page

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
            is GradeDialogDismissed -> state = state.copy(isGradeDialogVisible = false)
            is GradeDialogConfirmed -> {
                state = state.copy(isGradeDialogVisible = false)

                val updatedPage = superMemoUseCase(page = lastClickedPage, grade = state.selectedGrade)
                viewModelScope.launch {
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
        // TODO handle missed reviews

        val currEpochDay = LocalDate.now().toEpochDay()
        repository.getDuePagesForEpochDay(currEpochDay).onEach {
            state = state.copy(pagesDueToday = it)
        }.launchIn(viewModelScope)

        repository.getPages().onEach {
            state = state.copy(allPages = it)
        }.launchIn(viewModelScope)
    }
}
