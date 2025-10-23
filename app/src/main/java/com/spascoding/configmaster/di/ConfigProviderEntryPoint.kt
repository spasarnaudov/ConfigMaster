package com.spascoding.configmaster.di

import com.spascoding.configmastersdk.domain.usecases.GetConfigUseCase
import com.spascoding.configmastersdk.domain.usecases.InsertConfigUseCase
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@EntryPoint
@InstallIn(SingletonComponent::class)
interface ConfigProviderEntryPoint {

    fun getInsertConfigUseCase(): InsertConfigUseCase
    fun getConfigUseCase(): GetConfigUseCase

}