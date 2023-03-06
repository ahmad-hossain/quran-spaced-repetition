package com.github.ahmad_hossain.quranspacedrepetition.feature_settings.domain.use_case

import androidx.datastore.core.DataStore
import com.github.ahmad_hossain.quranspacedrepetition.feature_pages.domain.model.Page
import com.github.ahmad_hossain.quranspacedrepetition.feature_pages.domain.repository.PageRepository
import com.github.ahmad_hossain.quranspacedrepetition.feature_settings.domain.model.UserPreferences
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

class ChangePageRange @Inject constructor(
    private val dataStore: DataStore<UserPreferences>,
    private val repository: PageRepository
) {

    suspend operator fun invoke(newPageRange: IntRange) = withContext(Dispatchers.IO) {
        val data = dataStore.data.first()
        val oldPageRange = data.startPage..data.endPage
        Timber.d("oldPageRange: $oldPageRange newPageRange: $newPageRange")

        val pagesToAdd = newPageRange.subtract(oldPageRange)
        Timber.d("pagesToAdd: $pagesToAdd")
        pagesToAdd.forEach {
            repository.insertPage(Page(pageNumber = it))
        }

        val pagesToDelete = oldPageRange.subtract(newPageRange)
        Timber.d("pagesToDelete: $pagesToDelete")
        pagesToDelete.forEach {
            repository.deletePage(Page(pageNumber = it))
        }
    }
}