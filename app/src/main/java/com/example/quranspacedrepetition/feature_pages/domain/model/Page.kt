package com.example.quranspacedrepetition.feature_pages.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

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
    /** Date this Page is due for next review, or null if never reviewed */
    val dueDate: LocalDate? = null
) {
    val formattedDueDate: String
        get() = dueDate?.format(
            DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT)
        ).toString()

    object Converters {
        @TypeConverter
        fun epochDayToLocalDate(value: Long?): LocalDate? = value?.let { LocalDate.ofEpochDay(it) }

        @TypeConverter
        fun localDateToEpochDay(date: LocalDate?): Long? = date?.toEpochDay()
    }
}