package com.spascoding.configmaster.data.repository

import com.spascoding.configmaster.data.database.ConfigDao
import com.spascoding.configmaster.domain.models.ConfigEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import javax.inject.Inject

class ConfigRepository @Inject constructor(private val configDao: ConfigDao) {

    suspend fun getConfig(appId: String): List<ConfigEntity> {
        return withContext(Dispatchers.IO) {
            configDao.getConfig(appId)
        }
    }

    suspend fun getAllConfigs(): List<ConfigEntity> {
        return withContext(Dispatchers.IO) {
            configDao.getAllConfigs()
        }
    }

    // Insert configuration
    suspend fun saveConfig(appId: String, jsonData: String) {
        val configList = parseJsonToEntities(appId, jsonData)
        withContext(Dispatchers.IO) {
            configDao.saveConfig(configList)
        }
    }

    // Update configuration
    suspend fun updateConfig(appId: String, jsonData: String) {
        val configList = parseJsonToEntities(appId, jsonData)
        withContext(Dispatchers.IO) {
            configList.forEach { configDao.updateConfig(it) }
        }
    }

    // Delete configuration by appId
    suspend fun deleteConfig(appId: String) {
        withContext(Dispatchers.IO) {
            configDao.deleteConfig(appId)
        }
    }

    suspend fun getAllAppIds(): List<String> {
        return withContext(Dispatchers.IO) {
            configDao.getAllAppIds()
        }
    }

    // Helper function to parse JSON into ConfigEntity objects
    private fun parseJsonToEntities(appId: String, jsonData: String): List<ConfigEntity> {
        val jsonObject = JSONObject(jsonData)
        return jsonObject.keys().asSequence().map { key ->
            ConfigEntity(appId = appId, key = key, originalValue = jsonObject.getString(key))
        }.toList()
    }

}