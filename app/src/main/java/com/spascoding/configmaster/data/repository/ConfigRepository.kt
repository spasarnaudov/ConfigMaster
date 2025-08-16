package com.spascoding.configmaster.data.repository

import com.spascoding.configmaster.data.database.ConfigDao
import com.spascoding.configmaster.domain.models.ConfigEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ConfigRepository @Inject constructor(private val configDao: ConfigDao) {

    suspend fun getAllAppIds(): List<String> {
        return withContext(Dispatchers.IO) {
            configDao.getAllAppIds()
        }
    }

    suspend fun getConfig(appId: String): List<ConfigEntity> {
        return withContext(Dispatchers.IO) {
            configDao.getConfig(appId)
        }
    }

    suspend fun insertConfig(configList: List<ConfigEntity>) {
        configDao.insertConfig(configList)
    }

    suspend fun deleteConfig(appId: String, configList: List<String>) {
        configDao.deleteConfig(appId, configList)
    }

}