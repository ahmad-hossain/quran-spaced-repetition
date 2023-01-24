package com.example.quranspacedrepetition.feature_pages.presentation.pages

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import com.example.quranspacedrepetition.feature_pages.presentation.pages.PagesEvent.*
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class PagesViewModel @Inject constructor() : ViewModel() {

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
}
