package com.spascoding.configmaster.di

import com.spascoding.configmaster.domain.usecases.SaveConfigUseCase
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@EntryPoint
@InstallIn(SingletonComponent::class)
interface ConfigProviderEntryPoint {

    fun getSaveConfigUseCase(): SaveConfigUseCase

}