package com.github.ahmad_hossain.quranspacedrepetition.feature_pages.domain.repository

import comgithubahmadhossainquranspacedrepetition.Page

import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

interface PageRepository {
    fun getPages(): Flow<List<Page>>

    fun getPagesDueToday(): Flow<List<Page>>

    fun getPagesDueOn(date: LocalDate): Flow<List<Page>>

    suspend fun updatePage(page: Page)

    suspend fun insertPage(page: Page)

    suspend fun deletePage(page: Page)

    suspend fun checkpoint()
}