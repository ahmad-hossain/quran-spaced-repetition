package com.example.quranspacedrepetition.feature_pages.di

import android.annotation.SuppressLint
import android.app.Application
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.quranspacedrepetition.MainActivity
import com.example.quranspacedrepetition.R
import com.example.quranspacedrepetition.feature_pages.data.data_source.PageDatabase
import com.example.quranspacedrepetition.feature_pages.data.repository.PageRepositoryImpl
import com.example.quranspacedrepetition.feature_pages.domain.model.Page
import com.example.quranspacedrepetition.feature_pages.domain.receiver.AlarmReceiver
import com.example.quranspacedrepetition.feature_pages.domain.repository.PageRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
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

                val defaultPage = Page(pageNumber = 0)
                (1..611).forEach { pageNum ->
                    db.execSQL("INSERT INTO Page VALUES ($pageNum, ${defaultPage.interval}, ${defaultPage.repetitions}, ${defaultPage.eFactor}, ${defaultPage.dueDate})")
                }
            }
        }).build()
    }

    @Provides
    fun provideNotificationBuilder(@ApplicationContext context: Context): NotificationCompat.Builder {
        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val flags = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        } else {
            PendingIntent.FLAG_UPDATE_CURRENT
        }
        val pendingIntent: PendingIntent = PendingIntent.getActivity(context, 0, intent, flags)

        return NotificationCompat.Builder(context, AlarmReceiver.REMINDER_NOTIFICATION_CHANNEL_ID)
            .setOngoing(true)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle(context.getString(R.string.reminder_notification_title))
            .setContentIntent(pendingIntent)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
    }

    @Provides
    fun provideNotificationManager(@ApplicationContext context: Context) =
        NotificationManagerCompat.from(context)

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
}