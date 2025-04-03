package com.spascoding.configmaster.data.provider

import android.content.ContentProvider
import android.content.ContentValues
import android.content.UriMatcher
import android.database.Cursor
import android.database.MatrixCursor
import android.net.Uri
import android.util.Log

class ConfigProvider : ContentProvider() {

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
                val json = "{\"key1\": \"value1\", \"key2\": \"value2\"}"
                val matrixCursor = MatrixCursor(arrayOf("appId", "jsonData"))
                matrixCursor.addRow(arrayOf(appId, json))
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