package com.github.ahmad_hossain.quranspacedrepetition.feature_pages.data.repository

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import app.cash.sqldelight.db.SqlCursor
import app.cash.sqldelight.db.SqlDriver
import com.github.ahmad_hossain.quranspacedrepetition.PageDatabase
import com.github.ahmad_hossain.quranspacedrepetition.feature_pages.domain.repository.PageRepository
import comgithubahmadhossainquranspacedrepetition.Page
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import java.time.LocalDate

class PageRepositoryImpl(
    db: PageDatabase,
    private val driver: SqlDriver
) : PageRepository {
    private val queries = db.pageDatabaseQueries

    override fun getPages(): Flow<List<Page>> =
        queries.getPages().asFlow().mapToList(Dispatchers.IO)

    override fun getPagesDueToday(): Flow<List<Page>> = getPagesDueOn(LocalDate.now())

    override fun getPagesDueOn(date: LocalDate): Flow<List<Page>> =
        queries.getPagesDueOn(date.toEpochDay()).asFlow().mapToList(Dispatchers.IO)

    override suspend fun updatePage(page: Page) {
        withContext(Dispatchers.IO) {
            insertPage(page)
        }
    }

    override suspend fun insertPage(page: Page) {
        withContext(Dispatchers.IO) {
            queries.insertPage(
                page.pageNumber,
                page.interval,
                page.repetitions,
                page.eFactor,
                page.dueDate
            )
        }
    }

    override suspend fun deletePage(page: Page) {
        withContext(Dispatchers.IO) {
            queries.deletePageByNumber(page.pageNumber)
        }
    }

    override suspend fun checkpoint() {
        driver.executeQuery(
            identifier = null,
            sql = "PRAGMA wal_checkpoint(full)",
            mapper = SqlCursor::next,
            parameters = 0,
        )
    }
}