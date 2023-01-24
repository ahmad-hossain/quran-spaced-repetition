package com.example.quranspacedrepetition.feature_pages.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Page(
    /** Page Number in the Quran */
    @PrimaryKey
    val pageNumber: Int,
    /** Number of days to wait before the next review */
    val interval: Int = 0,
    /** Number of continuous *correct* repetitions */
    val repetitions: Int = 0,
    /** Easiness factor - calculated based off how easily info. is remembered */
    val eFactor: Double = 2.5,
    /** Start time (in unix seconds) for the next day this page is due for review */
    val dueDateUnixSecs: Long,
)