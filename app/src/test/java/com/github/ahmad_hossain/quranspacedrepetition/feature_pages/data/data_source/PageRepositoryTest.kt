package com.github.ahmad_hossain.quranspacedrepetition.feature_pages.data.data_source

import app.cash.sqldelight.adapter.primitive.IntColumnAdapter
import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import com.github.ahmad_hossain.quranspacedrepetition.PageDatabase
import com.github.ahmad_hossain.quranspacedrepetition.feature_pages.data.repository.PageRepositoryImpl
import com.github.ahmad_hossain.quranspacedrepetition.feature_pages.domain.repository.PageRepository
import com.github.ahmad_hossain.quranspacedrepetition.feature_pages.util.PageUtil
import com.google.common.truth.Truth.assertThat
import comgithubahmadhossainquranspacedrepetition.Page
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.time.LocalDate

@OptIn(ExperimentalCoroutinesApi::class)
class PageRepositoryTest {

    private lateinit var driver: JdbcSqliteDriver
    private lateinit var repo: PageRepository

    @BeforeEach
    fun setup() {
        JdbcSqliteDriver(JdbcSqliteDriver.IN_MEMORY).apply {
            driver = JdbcSqliteDriver(JdbcSqliteDriver.IN_MEMORY)
            PageDatabase.Schema.create(driver)
        }
        repo = PageRepositoryImpl(
            PageDatabase(
                driver, Page.Adapter(
                    pageNumberAdapter = IntColumnAdapter,
                    intervalAdapter = IntColumnAdapter,
                    repetitionsAdapter = IntColumnAdapter
                )
            ),
            driver
        )
    }

    @AfterEach
    fun teardown() {
        driver.close()
    }

    @Test
    fun getPagesDueToday() = runTest {
        val dueDate = LocalDate.ofEpochDay(15)
        val pagesDueToday = listOf(
            PageUtil.defaultPage(pageNumber = 0, dueDate = dueDate.toEpochDay()),
            PageUtil.defaultPage(pageNumber = 1, dueDate = dueDate.minusDays(5).toEpochDay()),
        )

        val otherPages = listOf(
            PageUtil.defaultPage(pageNumber = 2, dueDate = dueDate.plusDays(1).toEpochDay()),
            PageUtil.defaultPage(pageNumber = 3, dueDate = dueDate.plusDays(100).toEpochDay()),
            PageUtil.defaultPage(pageNumber = 4, dueDate = null),
        )
        (otherPages + pagesDueToday).forEach { repo.insertPage(it) }

        val data = repo.getPagesDueOn(dueDate).first()

        assertThat(data).containsExactlyElementsIn(pagesDueToday)
    }

    @Test
    fun deletePage() = runTest {
        val keep = listOf(
            PageUtil.defaultPage(pageNumber = 0),
            PageUtil.defaultPage(pageNumber = 1),
        )
        val delete = listOf(
            PageUtil.defaultPage(pageNumber = 2),
            PageUtil.defaultPage(pageNumber = 3),
        )

        (keep + delete).forEach { repo.insertPage(it) }
        delete.forEach { repo.deletePage(it) }

        assertThat(repo.getPages().first()).containsExactlyElementsIn(keep)
    }

    @Test
    fun insertPage() = runTest {
        val pages = listOf(
            PageUtil.defaultPage(pageNumber = 1),
            PageUtil.defaultPage(pageNumber = 9),
        )

        pages.forEach { repo.insertPage(it) }
        assertThat(repo.getPages().first()).containsExactlyElementsIn(pages)
    }
}