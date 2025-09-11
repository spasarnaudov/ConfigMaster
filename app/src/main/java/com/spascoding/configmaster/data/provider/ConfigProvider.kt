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
import com.spascoding.configmaster.domain.usecases.InsertConfigUseCase
import dagger.hilt.android.EntryPointAccessors
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.json.JSONObject

class ConfigProvider : ContentProvider() {

    private lateinit var insertConfigUseCase: InsertConfigUseCase
    private lateinit var getConfigUseCase: GetConfigUseCase

    companion object {
        const val AUTHORITY = "com.spascoding.configmaster.data.provider.ConfigProvider"

        const val CODE_CONFIG = 1

        private val uriMatcher = UriMatcher(UriMatcher.NO_MATCH).apply {
            addURI(AUTHORITY, null, CODE_CONFIG)
        }
    }

    override fun onCreate(): Boolean {
        Log.d(ConfigProvider::class.java.name, "ConfigProvider onCreate called")

        val appContext = context ?: return false
        insertConfigUseCase = EntryPointAccessors.fromApplication(
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
                val configName = selectionArgs?.get(0)
                val matrixCursor: MatrixCursor
                val jsonData = JSONObject()

                runBlocking {
                    // Fetch the updated config data from UseCase asynchronously
                    val configEntities = configName?.let { getConfigUseCase.execute(it) }

                    // Convert the result into JSON format
                    configEntities?.forEach { config ->
                        jsonData.put(config.parameter, config.modifiedValue)
                    }

                    // Create a cursor to return the data
                    matrixCursor = MatrixCursor(arrayOf("configName", "jsonData"))
                    matrixCursor.addRow(arrayOf(configName, jsonData.toString()))
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
                val configName = values?.getAsString("configName")
                val jsonData = values?.getAsString("jsonData")
                if (configName != null && jsonData != null) {
                    CoroutineScope(Dispatchers.IO).launch {
                        insertConfigUseCase.execute(configName, jsonData) // 👈 sync logic
                    }
                }
                uri
            }
            else -> throw IllegalArgumentException("Unknown URI: $uri")
        }
    }

    override fun update(uri: Uri, values: ContentValues?, selection: String?, selectionArgs: Array<String>?): Int {
        return 0
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<String>?): Int {
        return 0
    }

    override fun getType(uri: Uri): String? = null
}