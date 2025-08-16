package com.spascoding.configmaster.domain.usecases

import com.spascoding.configmaster.data.repository.ConfigRepository
import javax.inject.Inject

class DeleteConfigurationUseCase @Inject constructor(
    private val repository: ConfigRepository
) {
    suspend fun execute(appId: String) {
        repository.deleteConfiguration(appId)
    }
}