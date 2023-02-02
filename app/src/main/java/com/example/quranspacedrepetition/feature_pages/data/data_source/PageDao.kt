package com.example.quranspacedrepetition.feature_pages.data.data_source

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.quranspacedrepetition.feature_pages.domain.model.Page
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

@Dao
interface PageDao {

    @Query("SELECT * FROM Page")
    fun getPages(): Flow<List<Page>>

    @Query("SELECT * FROM Page WHERE dueDate=:currEpochDay")
    fun getPagesDueToday(currEpochDay: Long = LocalDate.now().toEpochDay()): Flow<List<Page>>

    @Query("SELECT * FROM Page WHERE dueDate<:currEpochDay")
    fun getOverduePages(currEpochDay: Long = LocalDate.now().toEpochDay()): List<Page>

    @Insert
    suspend fun insertPage(page: Page)

    @Update
    suspend fun updatePage(page: Page)
}