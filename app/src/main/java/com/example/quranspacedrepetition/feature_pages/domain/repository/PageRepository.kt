package com.example.quranspacedrepetition.feature_pages.domain.repository

import com.example.quranspacedrepetition.feature_pages.domain.model.Page
import kotlinx.coroutines.flow.Flow

interface PageRepository {
    fun getPages(): Flow<List<Page>>

    suspend fun updatePage(page: Page)
}