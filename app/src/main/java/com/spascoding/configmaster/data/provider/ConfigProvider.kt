package com.spascoding.configmaster.data.provider

import android.content.ContentProvider
import android.content.ContentValues
import android.content.UriMatcher
import android.database.Cursor
import android.net.Uri
import android.util.Log
import android.widget.Toast

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
        Log.d("ConfigProviderDebug", "ConfigProvider onCreate called")
        return true
    }

    override fun query(
        uri: Uri,
        projection: Array<String>?,
        selection: String?,
        selectionArgs: Array<String>?,
        sortOrder: String?
    ): Cursor? {
        when (uriMatcher.match(uri)) {
            CODE_CONFIG -> {
                Log.d("ConfigProviderDebug", "query uri $uri")
                Log.d("ConfigProviderDebug", "query values $selectionArgs")
                return null
            }
            else -> throw IllegalArgumentException("Unknown URI: $uri")
        }
    }

    // Other methods like insert, update, delete...

    override fun insert(uri: Uri, values: ContentValues?): Uri {
        return when (uriMatcher.match(uri)) {
            CODE_CONFIG -> {
                Log.d("ConfigProviderDebug", "insert uri $uri")
                Log.d("ConfigProviderDebug", "insert values $values")
                uri
            }
            else -> throw IllegalArgumentException("Unknown URI: $uri")
        }
    }

    // Update existing configuration
    override fun update(uri: Uri, values: ContentValues?, selection: String?, selectionArgs: Array<String>?): Int {
        when (uriMatcher.match(uri)) {
            CODE_CONFIG -> {
                Log.d("ConfigProviderDebug", "update uri $uri")
                Log.d("ConfigProviderDebug", "update values $values")
                return 0
            }
            else -> throw IllegalArgumentException("Unknown URI: $uri")
        }
    }

    // Delete configuration
    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<String>?): Int {
        when (uriMatcher.match(uri)) {
            CODE_CONFIG -> {
                Log.d("ConfigProviderDebug", "delete uri $uri")
                return 0
            }
            else -> throw IllegalArgumentException("Unknown URI: $uri")
        }
    }

    override fun getType(uri: Uri): String? = null
}