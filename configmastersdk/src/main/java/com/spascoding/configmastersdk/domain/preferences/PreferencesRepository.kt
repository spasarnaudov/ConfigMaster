package com.spascoding.configmastersdk.domain.preferences

interface PreferencesRepository {
    suspend fun saveSelectedConfig(configName: String)
    suspend fun getSelectedConfig(): String?
}