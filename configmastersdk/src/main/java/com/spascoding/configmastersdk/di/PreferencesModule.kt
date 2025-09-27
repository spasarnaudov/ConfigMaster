package com.spascoding.configmastersdk.di

import com.spascoding.configmastersdk.data.preferences.AppPreferences
import com.spascoding.configmastersdk.domain.preferences.PreferencesRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class PreferencesModule {

    @Binds
    @Singleton
    abstract fun bindPreferencesRepository(
        impl: AppPreferences
    ): PreferencesRepository
}