package com.github.ahmad_hossain.quranspacedrepetition.feature_settings.domain.use_case

import com.github.ahmad_hossain.quranspacedrepetition.feature_pages.domain.model.Page
import com.github.ahmad_hossain.quranspacedrepetition.feature_pages.domain.repository.PageRepository
import com.github.ahmad_hossain.quranspacedrepetition.feature_settings.domain.repository.SettingsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

class ChangePageRange @Inject constructor(
    private val settingsRepo: SettingsRepository,
    private val pageRepo: PageRepository
) {

    suspend operator fun invoke(newPageRange: IntRange) = withContext(Dispatchers.IO) {
        val data = settingsRepo.getDatastoreData().first()
        val oldPageRange = data.startPage..data.endPage
        Timber.d("oldPageRange: $oldPageRange newPageRange: $newPageRange")

        launch {
            val pagesToAdd = newPageRange.subtract(oldPageRange)
            Timber.d("pagesToAdd: $pagesToAdd")
            pagesToAdd.forEach {
                pageRepo.insertPage(Page(pageNumber = it))
            }
        }
        launch {
            val pagesToDelete = oldPageRange.subtract(newPageRange)
            Timber.d("pagesToDelete: $pagesToDelete")
            pagesToDelete.forEach {
                pageRepo.deletePage(Page(pageNumber = it))
            }
        }
    }
}