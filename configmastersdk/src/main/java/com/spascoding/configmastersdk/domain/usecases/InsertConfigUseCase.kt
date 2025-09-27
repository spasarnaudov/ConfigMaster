package com.spascoding.configmastersdk.domain.usecases

import com.spascoding.configmastersdk.data.repository.ConfigRepository
import com.spascoding.configmastersdk.domain.models.ConfigEntity
import org.json.JSONObject
import javax.inject.Inject

class InsertConfigUseCase @Inject constructor(
    private val repository: ConfigRepository
) {
    suspend fun execute(configs: List<ConfigEntity>) {
        repository.insertConfig(configs)
    }

    suspend fun execute(configName: String, newJson: String) {
        val newJsonObject = JSONObject(newJson)
        val newParameters = newJsonObject.keys().asSequence().toSet()

        val existing = repository.getConfig(configName)
        val existingParameters = existing.map { it.parameter }.toSet()

        val toInsert = mutableListOf<ConfigEntity>()
        val toUpdate = mutableListOf<ConfigEntity>()

        // Update existing keys
        existing.forEach { entity ->
            if (newParameters.contains(entity.parameter)) {
                val newOriginal = newJsonObject.getString(entity.parameter)
                val updatedEntity = entity.copy(
                    originalValue = newOriginal,
                    modifiedValue = entity.modifiedValue.ifEmpty { newOriginal } // preserve modified if not empty
                )
                toUpdate.add(updatedEntity)
            }
        }

        // Insert new keys
        (newParameters - existingParameters).forEach { parameter ->
            val value = newJsonObject.getString(parameter)
            toInsert.add(ConfigEntity(configName, parameter, value, value))
        }

        // Delete missing keys
        val toDelete = (existingParameters - newParameters).toList()

        repository.insertConfig(toUpdate)
        repository.insertConfig(toInsert)
        if (toDelete.isNotEmpty()) {
            repository.deleteConfigParameters(configName, toDelete)
        }
    }
}