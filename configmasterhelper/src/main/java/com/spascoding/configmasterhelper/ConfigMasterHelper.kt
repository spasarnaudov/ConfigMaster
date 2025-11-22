package com.spascoding.configmasterhelper

import android.content.ContentValues
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import org.json.JSONObject

object ConfigMasterHelper {

    private val CONFIG_URI: Uri =
        Uri.parse("content://com.spascoding.configmaster.data.provider.ConfigProvider")

    // ===================== PROVIDER COMMUNICATION =====================

    fun insertConfig(context: Context, configName: String, jsonData: String) {
        if (!isPackageInstalled(context, "com.spascoding.configmaster")) return

        val values = ContentValues().apply {
            put("configName", configName)
            put("jsonData", jsonData)
        }
        context.contentResolver.insert(CONFIG_URI, values)
    }

    fun fetchConfig(context: Context, configName: String): String? {
        val cursor = context.contentResolver.query(
            CONFIG_URI,
            null,
            null,
            arrayOf(configName),
            null
        )

        var jsonData: String? = null
        cursor?.use {
            if (it.moveToFirst()) {
                jsonData = it.getString(it.getColumnIndexOrThrow("jsonData"))
            }
        }
        return jsonData
    }

    private fun isPackageInstalled(context: Context, packageName: String): Boolean {
        return try {
            context.packageManager.getPackageInfo(packageName, 0)
            true
        } catch (e: PackageManager.NameNotFoundException) {
            false
        }
    }

    // ===================== CONFIG CACHE MANAGER =====================
    object Parameter {

        // configName -> (key -> value)
        private val cache = mutableMapOf<String, MutableMap<String, String>>()

        /**
         * MUST be called ONCE from Application or Activity
         */
        fun init(context: Context, vararg configNames: String) {
            configNames.forEach { configName ->
                preloadConfig(context, configName)
            }
        }

        private fun preloadConfig(context: Context, configName: String) {
            val json = fetchConfig(context, configName) ?: return

            val map = mutableMapOf<String, String>()
            val obj = JSONObject(json)

            obj.keys().forEach { key ->
                map[key] = obj.getString(key)
            }

            cache[configName] = map
        }

        /**
         * ✅ Access anywhere - NO CONTEXT REQUIRED
         */
        fun get(configName: String, key: String): String? {
            return cache[configName]?.get(key)
        }

        fun getAll(configName: String): Map<String, String> {
            return cache[configName] ?: emptyMap()
        }

        fun refresh(context: Context, configName: String) {
            preloadConfig(context, configName)
        }
    }
}