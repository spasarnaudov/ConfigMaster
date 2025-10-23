package com.spascoding.configmastersdk.domain.usecases

import com.spascoding.configmastersdk.data.repository.ConfigRepository
import com.spascoding.configmastersdk.domain.models.ConfigEntity
import com.spascoding.configmastersdk.domain.models.ConfigUpdateResult
import org.json.JSONObject
import javax.inject.Inject

class InsertConfigUseCase @Inject constructor(
    private val repository: ConfigRepository
) {
    suspend fun execute(configs: List<ConfigEntity>): Boolean {
        return try {
            if (configs.isEmpty()) return false
            repository.insertConfig(configs)
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    suspend fun execute(configName: String, newJson: String): ConfigUpdateResult {
        return try {
            val newJsonObject = JSONObject(newJson)
            val newParameters = newJsonObject.keys().asSequence().toSet()

            val existing = repository.getConfig(configName)
            val existingParameters = existing.map { it.parameter }.toSet()

            val toInsert = mutableListOf<ConfigEntity>()
            val toUpdate = mutableListOf<ConfigEntity>()
            val toDelete = (existingParameters - newParameters).toList()

            // Update existing keys
            existing.forEach { entity ->
                if (newParameters.contains(entity.parameter)) {
                    val newOriginal = newJsonObject.getString(entity.parameter)
                    if (entity.originalValue != newOriginal) {
                        val updatedEntity = entity.copy(
                            originalValue = newOriginal,
                            modifiedValue = entity.modifiedValue.ifEmpty { newOriginal }
                        )
                        toUpdate.add(updatedEntity)
                    }
                }
            }

            // Insert new keys
            (newParameters - existingParameters).forEach { parameter ->
                val value = newJsonObject.getString(parameter)
                toInsert.add(ConfigEntity(configName, parameter, value, value))
            }

            // Perform DB operations
            if (toInsert.isNotEmpty()) repository.insertConfig(toInsert)
            if (toUpdate.isNotEmpty()) repository.insertConfig(toUpdate)
            if (toDelete.isNotEmpty()) repository.deleteConfigParameters(configName, toDelete)

            // Return detailed result
            ConfigUpdateResult(
                inserted = toInsert.size,
                updated = toUpdate.size,
                deleted = toDelete.size
            )
        } catch (e: Exception) {
            e.printStackTrace()
            ConfigUpdateResult(0, 0, 0)
        }
    }
}