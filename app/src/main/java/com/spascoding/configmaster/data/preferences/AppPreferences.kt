package com.spascoding.configmaster.data.preferences

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.spascoding.configmaster.domain.preferences.PreferencesRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val Context.dataStore by preferencesDataStore(name = "config_prefs")

@Singleton
class AppPreferences @Inject constructor(
    @ApplicationContext private val context: Context
) : PreferencesRepository {

    companion object {
        private val SELECTED_CONFIG = stringPreferencesKey("selected_config")
    }

    override suspend fun saveSelectedConfig(configName: String) {
        context.dataStore.edit { prefs -> prefs[SELECTED_CONFIG] = configName }
    }

    override suspend fun getSelectedConfig(): String? {
        return context.dataStore.data
            .map { prefs -> prefs[SELECTED_CONFIG] }
            .first()
    }
}