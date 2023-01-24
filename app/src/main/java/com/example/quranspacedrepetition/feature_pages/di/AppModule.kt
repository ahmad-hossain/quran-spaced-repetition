package com.example.quranspacedrepetition.feature_pages.di

import android.app.Application
import androidx.room.Room
import com.example.quranspacedrepetition.feature_pages.data.data_source.PageDatabase
import com.example.quranspacedrepetition.feature_pages.data.repository.PageRepositoryImpl
import com.example.quranspacedrepetition.feature_pages.domain.repository.PageRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun providePageRepository(db: PageDatabase): PageRepository = PageRepositoryImpl(db.pageDao)

    @Provides
    @Singleton
    fun providePageDatabase(app: Application): PageDatabase {
        return Room.databaseBuilder(
            app,
            PageDatabase::class.java,
            PageDatabase.DATABASE_NAME
        ).build()
    }
}
