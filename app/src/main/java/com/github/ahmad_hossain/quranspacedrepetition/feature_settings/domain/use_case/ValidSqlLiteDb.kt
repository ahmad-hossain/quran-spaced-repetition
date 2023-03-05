package com.github.ahmad_hossain.quranspacedrepetition.feature_settings.domain.use_case
import android.app.Application
import android.net.Uri
import timber.log.Timber
import javax.inject.Inject

class ValidSqlLiteDb @Inject constructor(
    private val app: Application,
) {

    fun isValid(uri: Uri?): Boolean {
        val isValidSqlLiteDb = uri?.hasValidSQLiteDb() ?: false
        Timber.d("isValid: isValidSqlLiteDb=$isValidSqlLiteDb")

        return isValidSqlLiteDb
    }

    private fun Uri.hasValidSQLiteDb(): Boolean {
        try {
            val inputStream = app.contentResolver.openInputStream(this) ?: return false
            inputStream.use {
                val buffer = ByteArray(16)
                it.read(buffer, 0, 16)
                val str = String(buffer)
                return str == "SQLite format 3\u0000"
            }
        } catch (e: Exception) {
            Timber.e(e, "hasValidSQLiteDb")
            return false
        }
    }
}