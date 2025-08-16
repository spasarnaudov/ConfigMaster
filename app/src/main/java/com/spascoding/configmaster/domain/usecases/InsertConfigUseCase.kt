package com.spascoding.configmaster.domain.usecases

import com.spascoding.configmaster.data.repository.ConfigRepository
import com.spascoding.configmaster.domain.models.ConfigEntity
import org.json.JSONObject
import javax.inject.Inject

class InsertConfigUseCase @Inject constructor(
    private val repository: ConfigRepository
) {
    suspend fun execute(configs: List<ConfigEntity>) {
        repository.insertConfig(configs)
    }

    suspend fun execute(appId: String, newJson: String) {
        val newJsonObject = JSONObject(newJson)
        val newKeys = newJsonObject.keys().asSequence().toSet()

        val existing = repository.getConfig(appId)
        val existingKeys = existing.map { it.key }.toSet()

        val toInsert = mutableListOf<ConfigEntity>()
        val toUpdate = mutableListOf<ConfigEntity>()

        // Update existing keys
        existing.forEach { entity ->
            if (newKeys.contains(entity.key)) {
                val newOriginal = newJsonObject.getString(entity.key)
                val updatedEntity = entity.copy(
                    originalValue = newOriginal,
                    modifiedValue = entity.modifiedValue.ifEmpty { newOriginal } // preserve modified if not empty
                )
                toUpdate.add(updatedEntity)
            }
        }

        // Insert new keys
        (newKeys - existingKeys).forEach { key ->
            val value = newJsonObject.getString(key)
            toInsert.add(ConfigEntity(appId, key, value, value))
        }

        // Delete missing keys
        val toDelete = (existingKeys - newKeys).toList()

        repository.insertConfig(toUpdate)
        repository.insertConfig(toInsert)
        if (toDelete.isNotEmpty()) {
            repository.deleteConfig(appId, toDelete)
        }
    }
}
