package com.example.quranspacedrepetition.feature_pages.data.data_source

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Update
import com.example.quranspacedrepetition.feature_pages.domain.model.Page
import kotlinx.coroutines.flow.Flow

@Dao
interface PageDao {

    @Query("SELECT * FROM Page")
    fun getPages(): Flow<List<Page>>

    @Update
    suspend fun updatePage(page: Page)
}