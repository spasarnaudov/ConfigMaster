package com.spascoding.configmasterhelper.javahelper;

import android.content.ContentValues;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import org.json.JSONObject;

public class ConfigMasterHelper {

    private static final Uri CONFIG_URI =
            Uri.parse("content://com.spascoding.configmaster.data.provider.ConfigProvider");

    /**
     * Insert or update configuration into ConfigMaster
     */
    public static void insertConfig(Context context, String configName, String jsonData) {

        if (!isPackageInstalled(context, "com.spascoding.configmaster")) {
            return;
        }

        ContentValues values = new ContentValues();
        values.put("configName", configName);
        values.put("jsonData", jsonData);

        context.getContentResolver().insert(CONFIG_URI, values);
    }

    /**
     * Fetch configuration from ConfigMaster
     */
    public static String fetchConfig(Context context, String configName) {
        Cursor cursor = context.getContentResolver().query(
                CONFIG_URI,
                null,
                null,
                new String[]{configName},
                null
        );

        String jsonData = null;
        if (cursor != null) {
            try {
                if (cursor.moveToFirst()) {
                    jsonData = cursor.getString(cursor.getColumnIndexOrThrow("jsonData"));
                    Log.d("ConfigMasterHelper", "Fetched config = " + jsonData);
                }
            } finally {
                cursor.close();
            }
        }
        return jsonData;
    }

    /**
     * Fetch a single parameter value by key from configuration JSON
     */
    public static String fetchConfigParam(Context context, String configName, String paramKey) {
        String jsonData = fetchConfig(context, configName);
        if (jsonData == null || jsonData.isEmpty()) {
            return null;
        }

        try {
            JSONObject jsonObject = new JSONObject(jsonData);
            if (jsonObject.has(paramKey)) {
                return jsonObject.getString(paramKey);
            } else {
                return null;
            }
        } catch (Exception e) {
            Log.e("ConfigMasterHelper", "Error parsing JSON: " + e.getMessage());
            return null;
        }
    }

    private static boolean isPackageInstalled(Context context, String packageName) {
        PackageManager packageManager = context.getPackageManager();
        try {
            packageManager.getPackageInfo(packageName, 0);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }
}

