package com.github.ahmad_hossain.quranspacedrepetition.common.di

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.Application
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.activity.ComponentActivity
import androidx.core.app.NotificationManagerCompat
import androidx.datastore.core.DataStore
import androidx.datastore.dataStore
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.github.ahmad_hossain.quranspacedrepetition.feature_pages.data.data_source.PageDatabase
import com.github.ahmad_hossain.quranspacedrepetition.feature_pages.data.repository.PageRepositoryImpl
import com.github.ahmad_hossain.quranspacedrepetition.feature_pages.domain.model.Page
import com.github.ahmad_hossain.quranspacedrepetition.feature_pages.domain.receiver.AlarmReceiver
import com.github.ahmad_hossain.quranspacedrepetition.feature_pages.domain.repository.PageRepository
import com.github.ahmad_hossain.quranspacedrepetition.feature_settings.data.data_source.UserPreferencesSerializer
import com.github.ahmad_hossain.quranspacedrepetition.feature_settings.domain.model.UserPreferences
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.first
import timber.log.Timber
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
        ).addCallback(object : RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                Timber.d("onCreate RoomDatabase")
                super.onCreate(db)

                runBlocking {
                    val userPrefs = app.dataStore.data.first()
                    val defaultPage = Page(pageNumber = 0)

                    (userPrefs.startPage..userPrefs.endPage).forEach { pageNum ->
                        db.execSQL("INSERT INTO Page VALUES ($pageNum, ${defaultPage.interval}, ${defaultPage.repetitions}, ${defaultPage.eFactor}, ${defaultPage.dueDate})")
                    }
                }
            }
        }).build()
    }

    @Provides
    fun provideNotificationManagerCompat(@ApplicationContext context: Context) =
        NotificationManagerCompat.from(context)

    @Provides
    fun provideAlarmManager(@ApplicationContext context: Context) =
        context.getSystemService(ComponentActivity.ALARM_SERVICE) as AlarmManager

    /** Already doing API Check */
    @SuppressLint("InlinedApi")
    @Provides
    fun provideAlarmPendingIntent(@ApplicationContext context: Context): PendingIntent {
        val intent = Intent(context, AlarmReceiver::class.java)
        val flags = when (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            true -> PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            false -> PendingIntent.FLAG_UPDATE_CURRENT
        }
        return PendingIntent.getBroadcast(context, 0, intent, flags)
    }

    private val Context.dataStore: DataStore<UserPreferences> by dataStore(
        fileName = "user-prefs.json",
        serializer = UserPreferencesSerializer
    )

    @Provides
    fun provideDataStore(appContext: Application): DataStore<UserPreferences> {
        return appContext.dataStore
    }
}