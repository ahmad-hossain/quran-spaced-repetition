package com.github.ahmad_hossain.quranspacedrepetition.feature_settings.domain.model

import com.github.ahmad_hossain.quranspacedrepetition.feature_settings.data.data_source.LocalTimeSerializer
import kotlinx.serialization.Serializable
import java.time.LocalTime

@Serializable
data class UserPreferences(
    val startPage: Int = 1,
    val endPage: Int = 611,
    @Serializable(with = LocalTimeSerializer::class)
    val notificationTime: LocalTime = LocalTime.of(0, 0)
)