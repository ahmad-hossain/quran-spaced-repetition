package com.example.quranspacedrepetition.feature_pages.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import java.time.LocalDate

@TypeConverters(Page.Converters::class)
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
    /** Date this Page is due for next review, or epoch-day of [DEFAULT_DUE_DATE_EPOCH_DAY] if never reviewed */
    val dueDate: LocalDate = LocalDate.ofEpochDay(DEFAULT_DUE_DATE_EPOCH_DAY)
) {

    object Converters {
        @TypeConverter
        fun epochDayToLocalDate(value: Long): LocalDate = LocalDate.ofEpochDay(value)

        @TypeConverter
        fun localDateToEpochDay(date: LocalDate): Long = date.toEpochDay()
    }
    companion object {
        const val DEFAULT_DUE_DATE_EPOCH_DAY = 0L
    }
}