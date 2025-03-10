package com.github.ahmad_hossain.quranspacedrepetition.feature_pages.domain.use_case

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.Intent.ACTION_VIEW
import android.net.Uri
import androidx.core.content.ContextCompat.startActivity
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class OpenPageInQuranApp @Inject constructor(
    @ApplicationContext private val context: Context
) {

    fun execute(pageNumber: Int) {
        try {
            quranAppIntent.apply {
                putExtra("pageNumber", pageNumber)
            }
            startActivity(context, quranAppIntent, null)
        } catch (e: Exception) {
            startActivity(context, quranPlayStoreListingIntent, null)
        }
    }


    private companion object {
        const val APPLICATION_ID_QURAN_APP = "com.github.ahmad_hossain.quran15line"
        val quranAppIntent = Intent().apply {
            action = ACTION_VIEW
            component =
                ComponentName(APPLICATION_ID_QURAN_APP, "$APPLICATION_ID_QURAN_APP.MainActivity")
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }
        val quranPlayStoreListingIntent = Intent(ACTION_VIEW).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
            data = Uri.parse(
                "https://play.google.com/store/apps/details?id=$APPLICATION_ID_QURAN_APP"
            )
            setPackage("com.android.vending")
        }

    }
}