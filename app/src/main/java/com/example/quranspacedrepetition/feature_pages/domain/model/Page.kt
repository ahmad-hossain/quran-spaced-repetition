package com.example.quranspacedrepetition.feature_pages.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Page(
    @PrimaryKey
    val pageNumber: Int,
    val interval: Int = 0,
    val repetitions: Int = 0,
    val eFactor: Double = 2.5,
    val dueDate: Long,
)
