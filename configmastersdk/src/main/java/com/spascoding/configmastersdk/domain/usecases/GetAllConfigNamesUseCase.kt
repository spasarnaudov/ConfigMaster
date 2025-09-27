package com.spascoding.configmastersdk.domain.usecases

import com.spascoding.configmastersdk.data.repository.ConfigRepository
import jakarta.inject.Inject

class GetAllConfigNamesUseCase @Inject constructor(
    private val repository: ConfigRepository
) {
    suspend fun execute(): List<String> {
        return repository.getAllConfigNames()
    }
}