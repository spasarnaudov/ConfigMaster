package com.spascoding.configmaster.domain.usecases

import com.spascoding.configmaster.data.repository.ConfigRepository
import com.spascoding.configmaster.domain.models.ConfigEntity
import javax.inject.Inject

class GetConfigUseCase @Inject constructor(private val repository: ConfigRepository) {
    suspend fun execute(appId: String): List<ConfigEntity> {
        return repository.getConfig(appId)
    }
}