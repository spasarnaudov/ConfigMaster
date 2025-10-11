package com.spascoding.configmastersdk

import android.content.Context
import androidx.room.Room
import com.spascoding.configmastersdk.data.database.ConfigDatabase
import com.spascoding.configmastersdk.data.preferences.AppPreferences
import com.spascoding.configmastersdk.data.repository.ConfigRepository
import com.spascoding.configmastersdk.domain.usecases.GetConfigUseCase
import com.spascoding.configmastersdk.domain.usecases.InsertConfigUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject

object ConfigMasterSdk {

    private var database: ConfigDatabase? = null
    private var repository: ConfigRepository? = null
    private var preferences: AppPreferences? = null
    private var initialized = false
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    @Synchronized
    fun initialize(context: Context) {
        if (initialized) return

        val appContext = context.applicationContext

        val db = Room.databaseBuilder(
            appContext,
            ConfigDatabase::class.java,
            "config_database"
        ).fallbackToDestructiveMigration().build()

        val dao = db.configDao()
        val repo = ConfigRepository(dao)
        val prefs = AppPreferences(appContext)

        database = db
        repository = repo
        preferences = prefs
        initialized = true
    }

    fun provideRepository(): ConfigRepository =
        repository ?: error("ConfigMasterSdk not initialized. Call ConfigMasterSdk.initialize(context) first.")

    fun providePreferences(): AppPreferences =
        preferences ?: error("ConfigMasterSdk not initialized. Call ConfigMasterSdk.initialize(context) first.")

    internal fun isInitialized() = initialized

    /** Async helper to insert JSON */
    fun insertJsonAsync(configName: String, json: String) {
        scope.launch {
            InsertConfigUseCase(provideRepository()).execute(configName, json)
        }
    }

    /** Suspend helper to insert JSON */
    suspend fun insertJson(configName: String, json: String) {
        InsertConfigUseCase(provideRepository()).execute(configName, json)
    }

    /** Suspend: get all parameters as JSON string */
    suspend fun getModifiedJson(configName: String): String {
        val configs = GetConfigUseCase(provideRepository()).execute(configName)
        val json = JSONObject()
        configs.forEach {
            json.put(it.parameter, it.modifiedValue.ifEmpty { it.originalValue })
        }
        return json.toString()
    }

    /** Async: get all parameters as JSON string */
    fun getModifiedJsonAsync(configName: String, onResult: (String) -> Unit) {
        scope.launch {
            val configs = GetConfigUseCase(provideRepository()).execute(configName)
            val json = JSONObject()
            configs.forEach {
                json.put(it.parameter, it.modifiedValue.ifEmpty { it.originalValue })
            }
            withContext(Dispatchers.Main) {
                onResult(json.toString())
            }
        }
    }

    /** Suspend: get a single parameter as String? */
    suspend fun getModifiedParameter(configName: String, parameter: String): String? {
        val configs = GetConfigUseCase(provideRepository()).execute(configName)
        return configs.find { it.parameter == parameter }?.modifiedValue?.takeIf { it.isNotEmpty() }
            ?: configs.find { it.parameter == parameter }?.originalValue
    }

    /** Async: get a single parameter as String? */
    fun getModifiedParameterAsync(
        configName: String,
        parameter: String,
        onResult: (String?) -> Unit
    ) {
        scope.launch {
            val configs = GetConfigUseCase(provideRepository()).execute(configName)
            val value = configs.find { it.parameter == parameter }?.modifiedValue?.takeIf { it.isNotEmpty() }
                ?: configs.find { it.parameter == parameter }?.originalValue
            withContext(Dispatchers.Main) {
                onResult(value)
            }
        }
    }
}
