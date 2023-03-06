package com.github.ahmad_hossain.quranspacedrepetition.feature_pages.data.data_source

import androidx.room.*
import androidx.sqlite.db.SupportSQLiteQuery
import com.github.ahmad_hossain.quranspacedrepetition.feature_pages.domain.model.Page
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

@Dao
interface PageDao {

    @Query("SELECT * FROM Page")
    fun getPages(): Flow<List<Page>>

    @Query("SELECT * FROM Page WHERE dueDate<=:currEpochDay")
    fun getPagesDueToday(currEpochDay: Long = LocalDate.now().toEpochDay()): Flow<List<Page>>

    @Insert
    suspend fun insertPage(page: Page)

    @Update
    suspend fun updatePage(page: Page)

    @Delete
    suspend fun deletePage(page: Page)

    @RawQuery
    fun checkpoint(supportSQLiteQuery: SupportSQLiteQuery): Int
}