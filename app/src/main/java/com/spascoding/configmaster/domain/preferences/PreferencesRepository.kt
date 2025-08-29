package com.spascoding.configmaster.domain.preferences

interface PreferencesRepository {
    suspend fun saveSelectedConfig(configName: String)
    suspend fun getSelectedConfig(): String?
}