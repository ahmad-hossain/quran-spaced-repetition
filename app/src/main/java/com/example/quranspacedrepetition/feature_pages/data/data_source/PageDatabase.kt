package com.example.quranspacedrepetition.feature_pages.data.data_source

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.quranspacedrepetition.feature_pages.domain.model.Page

@Database(
    entities = [Page::class],
    version = 1,
    exportSchema = false
)
abstract class PageDatabase : RoomDatabase() {

    abstract val pageDao: PageDao

    companion object {
        const val DATABASE_NAME = "page_db"
    }
}