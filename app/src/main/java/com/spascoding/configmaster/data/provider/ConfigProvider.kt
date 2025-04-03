package com.spascoding.configmaster.data.provider

import android.content.ContentProvider
import android.content.ContentValues
import android.content.UriMatcher
import android.database.Cursor
import android.database.MatrixCursor
import android.net.Uri
import android.util.Log
import com.spascoding.configmaster.di.ConfigProviderEntryPoint
import com.spascoding.configmaster.domain.usecases.GetConfigUseCase
import com.spascoding.configmaster.domain.usecases.SaveConfigUseCase
import dagger.hilt.android.EntryPointAccessors
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.json.JSONObject

class ConfigProvider : ContentProvider() {

    private lateinit var saveConfigUseCase: SaveConfigUseCase
    private lateinit var getConfigUseCase: GetConfigUseCase

    companion object {
        const val AUTHORITY = "com.spascoding.configmaster.data.provider.ConfigProvider"
        val CONTENT_URI: Uri = Uri.parse("content://$AUTHORITY/config")

        const val CODE_CONFIG = 1
        const val CODE_CONFIG_ITEM = 2

        private val uriMatcher = UriMatcher(UriMatcher.NO_MATCH).apply {
            addURI(AUTHORITY, "config", CODE_CONFIG)
            addURI(AUTHORITY, "config/*", CODE_CONFIG_ITEM)
        }
    }

    override fun onCreate(): Boolean {
        Log.d(ConfigProvider::class.java.name, "ConfigProvider onCreate called")

        val appContext = context ?: return false
        saveConfigUseCase = EntryPointAccessors.fromApplication(
            appContext,
            ConfigProviderEntryPoint::class.java
        ).getSaveConfigUseCase()
        getConfigUseCase = EntryPointAccessors.fromApplication(
            appContext,
            ConfigProviderEntryPoint::class.java
        ).getConfigUseCase()

        return true
    }

    override fun query(
        uri: Uri,
        projection: Array<String>?,
        selection: String?,
        selectionArgs: Array<String>?,
        sortOrder: String?
    ): Cursor {
        when (uriMatcher.match(uri)) {
            CODE_CONFIG -> {
                val appId = selectionArgs?.get(0)
                val matrixCursor: MatrixCursor
                val jsonData = JSONObject()

                runBlocking {
                    // Fetch the updated config data from UseCase asynchronously
                    val configEntities = appId?.let { getConfigUseCase.execute(it) }

                    // Convert the result into JSON format
                    configEntities?.forEach { config ->
                        jsonData.put(config.key, config.modifiedValue)
                    }

                    // Create a cursor to return the data
                    matrixCursor = MatrixCursor(arrayOf("appId", "jsonData"))
                    matrixCursor.addRow(arrayOf(appId, jsonData.toString()))
                }

                return matrixCursor
            }
            else -> throw IllegalArgumentException("Unknown URI: $uri")
        }
    }

    // Other methods like insert, update, delete...

    override fun insert(uri: Uri, values: ContentValues?): Uri {
        return when (uriMatcher.match(uri)) {
            CODE_CONFIG -> {
                val appId = values?.getAsString("appId")
                val jsonData = values?.getAsString("jsonData")
                Log.d(ConfigProvider::class.java.name, "insert")
                Log.d(ConfigProvider::class.java.name, "$appId")
                Log.d(ConfigProvider::class.java.name, "$jsonData")
                CoroutineScope(Dispatchers.IO).launch {
                    saveConfigUseCase.execute(appId.toString(), jsonData.toString())
                }
                uri
            }
            else -> throw IllegalArgumentException("Unknown URI: $uri")
        }
    }

    // Update existing configuration
    override fun update(uri: Uri, values: ContentValues?, selection: String?, selectionArgs: Array<String>?): Int {
        when (uriMatcher.match(uri)) {
            CODE_CONFIG -> {
                val appId = values?.getAsString("appId")
                val jsonData = values?.getAsString("jsonData")
                Log.d(ConfigProvider::class.java.name, "update")
                Log.d(ConfigProvider::class.java.name, "$appId")
                Log.d(ConfigProvider::class.java.name, "$jsonData")
                return 0
            }
            else -> throw IllegalArgumentException("Unknown URI: $uri")
        }
    }

    // Delete configuration
    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<String>?): Int {
        when (uriMatcher.match(uri)) {
            CODE_CONFIG -> {
                val appId = selectionArgs?.get(0)
                Log.d(ConfigProvider::class.java.name, "delete")
                Log.d(ConfigProvider::class.java.name, "$appId")
                return 0
            }
            else -> throw IllegalArgumentException("Unknown URI: $uri")
        }
    }

    override fun getType(uri: Uri): String? = null
}