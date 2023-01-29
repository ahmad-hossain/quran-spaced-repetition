package com.example.quranspacedrepetition.feature_pages.data.repository

import com.example.quranspacedrepetition.feature_pages.data.data_source.PageDao
import com.example.quranspacedrepetition.feature_pages.domain.model.Page
import com.example.quranspacedrepetition.feature_pages.domain.repository.PageRepository
import kotlinx.coroutines.flow.Flow

class PageRepositoryImpl(
    private val pageDao: PageDao
) : PageRepository {
    override fun getPages(): Flow<List<Page>> = pageDao.getPages()
    override fun getDuePagesForEpochDay(epochDay: Long): Flow<List<Page>> =
        pageDao.getDuePagesForEpochDay(epochDay)

    override fun getOverduePages(): List<Page> = pageDao.getOverduePages()

    override suspend fun updatePage(page: Page) {
        pageDao.updatePage(page)
    }
}