package com.spascoding.configmastersdk.domain.usecases

import com.spascoding.configmastersdk.data.repository.ConfigRepository
import javax.inject.Inject

class DeleteConfigUseCase @Inject constructor(
    private val repository: ConfigRepository
) {
    suspend fun execute(configName: String) {
        repository.deleteConfig(configName)
    }
}