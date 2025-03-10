package com.github.ahmad_hossain.quranspacedrepetition.feature_settings.domain.use_case

import android.content.Context
import android.content.Intent
import android.net.Uri
import com.github.ahmad_hossain.quranspacedrepetition.R
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class OpenEmailWithDeveloper @Inject constructor(
    @ApplicationContext private val context: Context
) {

    fun execute() {
        val emailChooserIntent = Intent.createChooser(
            openEmailIntent,
            context.getString(R.string.choose_an_email_app)
        )
        emailChooserIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK

        context.startActivity(emailChooserIntent)
    }

    private companion object {
        const val STRING_DEVELOPER_EMAIL = "ahmadh.developer@gmail.com"
        const val STRING_EMAIL_SUBJECT = "[Quran Hifz Revision] User Feedback"

        val openEmailIntent = Intent(Intent.ACTION_SENDTO).apply {
            data = Uri.parse("mailto:")
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
            putExtra(Intent.EXTRA_EMAIL, arrayOf(STRING_DEVELOPER_EMAIL))
            putExtra(Intent.EXTRA_SUBJECT, STRING_EMAIL_SUBJECT)
        }

    }
}