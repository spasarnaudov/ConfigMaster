package com.spascoding.configmaster.domain.usecases

import com.spascoding.configmaster.data.repository.ConfigRepository
import jakarta.inject.Inject

class GetAllAppIdsUseCase @Inject constructor(
    private val repository: ConfigRepository
) {
    suspend fun execute(): List<String> {
        return repository.getAllAppIds()
    }
}
