package com.spascoding.configmaster.data.repository

import com.spascoding.configmaster.data.database.ConfigDao
import com.spascoding.configmaster.domain.models.ConfigEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ConfigRepository @Inject constructor(private val configDao: ConfigDao) {

    suspend fun getAllConfigNames(): List<String> {
        return withContext(Dispatchers.IO) {
            configDao.getAllConfigNames()
        }
    }

    suspend fun getConfig(configName: String): List<ConfigEntity> {
        return withContext(Dispatchers.IO) {
            configDao.getConfig(configName)
        }
    }

    suspend fun insertConfig(configs: List<ConfigEntity>) {
        configDao.insertConfig(configs)
    }

    suspend fun deleteConfigParameters(configName: String, configNames: List<String>) {
        configDao.deleteConfig(configName, configNames)
    }

    suspend fun deleteConfig(configName: String) {
        configDao.deleteConfig(configName)
    }

}