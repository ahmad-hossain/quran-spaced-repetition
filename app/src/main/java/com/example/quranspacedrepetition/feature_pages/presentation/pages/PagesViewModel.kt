package com.example.quranspacedrepetition.feature_pages.presentation.pages

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.quranspacedrepetition.feature_pages.domain.repository.PageRepository
import com.example.quranspacedrepetition.feature_pages.presentation.pages.PagesEvent.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import timber.log.Timber
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class PagesViewModel @Inject constructor(
    repository: PageRepository
) : ViewModel() {

    var state by mutableStateOf(PagesState())
        private set

    fun onEvent(event: PagesEvent) {
        Timber.d("%s : %s", event::class.simpleName, event.toString())

        when (event) {
            is PageClicked -> TODO()
            is TodayChipClicked -> {
                state = state.copy(
                    isTodayChipSelected = true,
                    isAllChipSelected = false
                )
            }
            is AllChipClicked -> {
                state = state.copy(
                    isTodayChipSelected = false,
                    isAllChipSelected = true
                )
            }
        }
    }

    init {
        val currEpochDay = LocalDate.now().toEpochDay()
        repository.getDuePagesForEpochDay(currEpochDay).onEach {
            state = state.copy(pagesDueToday = it)
        }.launchIn(viewModelScope)

        repository.getPages().onEach {
            state = state.copy(allPages = it)
        }.launchIn(viewModelScope)
    }
}
