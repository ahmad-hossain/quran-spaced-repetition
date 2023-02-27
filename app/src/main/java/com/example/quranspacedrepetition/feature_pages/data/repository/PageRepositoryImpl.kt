package com.example.quranspacedrepetition.feature_pages.data.repository

import androidx.sqlite.db.SimpleSQLiteQuery
import com.example.quranspacedrepetition.feature_pages.data.data_source.PageDao
import com.example.quranspacedrepetition.feature_pages.domain.model.Page
import com.example.quranspacedrepetition.feature_pages.domain.repository.PageRepository
import kotlinx.coroutines.flow.Flow

class PageRepositoryImpl(
    private val pageDao: PageDao
) : PageRepository {
    override fun getPages(): Flow<List<Page>> = pageDao.getPages()

    override fun getPagesDueToday(): Flow<List<Page>> = pageDao.getPagesDueToday()

    override suspend fun updatePage(page: Page) {
        pageDao.updatePage(page)
    }

    override suspend fun checkpoint() {
        val checkpointQuery = "pragma wal_checkpoint(full)"
        pageDao.checkpoint(SimpleSQLiteQuery(checkpointQuery))
    }
}