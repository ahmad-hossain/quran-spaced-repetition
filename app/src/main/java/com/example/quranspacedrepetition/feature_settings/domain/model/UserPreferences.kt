package com.example.quranspacedrepetition.feature_settings.domain.model

import com.example.quranspacedrepetition.feature_settings.data.data_source.LocalTimeSerializer
import kotlinx.serialization.Serializable
import java.time.LocalTime

@Serializable
data class UserPreferences(
    val startPage: Int = 0,
    val endPage: Int = 0,
    @Serializable(with = LocalTimeSerializer::class)
    val notificationTime: LocalTime = LocalTime.of(0, 0)
)