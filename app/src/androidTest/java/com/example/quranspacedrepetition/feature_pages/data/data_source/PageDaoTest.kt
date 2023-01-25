package com.example.quranspacedrepetition.feature_pages.data.data_source

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.example.quranspacedrepetition.feature_pages.domain.model.Page
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.time.LocalDate

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(AndroidJUnit4::class)
@SmallTest
class PageDaoTest {

    private lateinit var database: PageDatabase
    private lateinit var dao: PageDao

    @Before
    fun setup() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(), PageDatabase::class.java
        ).allowMainThreadQueries().build()
        dao = database.pageDao
    }

    @After
    fun teardown() {
        database.close()
    }

    @Test
    fun getDuePagesForEpochDay() = runTest {
        val pageDueToday = Page(pageNumber = 0, dueDate = LocalDate.now())

        val otherPages = listOf(
            Page(pageNumber = 1, dueDate = LocalDate.now().minusDays(5)),
            Page(pageNumber = 2, dueDate = LocalDate.now().plusDays(1)),
        )
        (otherPages + pageDueToday).forEach { dao.insertPage(it) }

        val data = dao.getDuePagesForEpochDay(LocalDate.now().toEpochDay()).first()

        assertThat(data).containsExactly(pageDueToday)
    }
}