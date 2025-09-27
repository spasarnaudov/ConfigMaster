package com.spascoding.configmastersdk.domain.usecases

import com.spascoding.configmastersdk.data.repository.ConfigRepository
import com.spascoding.configmastersdk.domain.models.ConfigEntity
import javax.inject.Inject

class GetConfigUseCase @Inject constructor(private val repository: ConfigRepository) {
    suspend fun execute(configName: String): List<ConfigEntity> {
        return repository.getConfig(configName)
    }
}