package com.example.quranspacedrepetition.feature_settings.domain.model

import com.example.quranspacedrepetition.feature_settings.data.data_source.LocalTimeSerializer
import kotlinx.serialization.Serializable
import java.time.LocalTime

@Serializable
data class UserPreferences(
    val startPage: Int = DEFAULT_START_PAGE,
    val endPage: Int = DEFAULT_END_PAGE,
    @Serializable(with = LocalTimeSerializer::class)
    val notificationTime: LocalTime = LocalTime.of(0, 0)
) {
    companion object {
        const val DEFAULT_START_PAGE = 1
        const val DEFAULT_END_PAGE = 611
    }
}