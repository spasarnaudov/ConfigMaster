package com.spascoding.configmaster.domain.preferences

interface PreferencesRepository {
    suspend fun saveSelectedAppId(appId: String)
    suspend fun getSelectedAppId(): String?
}