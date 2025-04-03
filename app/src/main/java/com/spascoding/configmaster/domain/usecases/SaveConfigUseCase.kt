package com.spascoding.configmaster.domain.usecases

import com.spascoding.configmaster.data.repository.ConfigRepository
import javax.inject.Inject

class SaveConfigUseCase @Inject constructor(private val repository: ConfigRepository) {

    suspend fun execute(appId: String, jsonData: String) {
        repository.saveConfig(appId, jsonData)
    }

}