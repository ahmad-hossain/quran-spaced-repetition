package com.github.ahmad_hossain.quranspacedrepetition.feature_settings.di

import com.github.ahmad_hossain.quranspacedrepetition.feature_settings.data.repository.SettingsRepositoryImpl
import com.github.ahmad_hossain.quranspacedrepetition.feature_settings.domain.repository.SettingsRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
interface SettingsModule {

    @Binds
    @ViewModelScoped
    fun bindSettingsRepository(impl: SettingsRepositoryImpl): SettingsRepository
}