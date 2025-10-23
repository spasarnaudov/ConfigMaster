package com.spascoding.configmaster.utils

import android.Manifest
import android.R
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.spascoding.configmastersdk.presentation.ConfigMasterActivity
import com.spascoding.configmastersdk.presentation.ConfigMasterHiltActivity

object NotificationHelper {

    private const val CONFIG_CHANGES_CHANNEL = "config_changes_channel"

    fun showConfigUpdatedNotification(
        context: Context,
        configName: String,
        inserted: Int,
        updated: Int,
        deleted: Int
    ) {
        // Create notification channel (once)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CONFIG_CHANGES_CHANNEL,
                "ConfigMaster Updates",
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = "Notifies when configurations are changed."
            }
            context.getSystemService(NotificationManager::class.java)
                ?.createNotificationChannel(channel)
        }

        // Build dynamic message
        val messageParts = mutableListOf<String>()
        if (inserted > 0) messageParts.add("added $inserted")
        if (updated > 0) messageParts.add("updated $updated")
        if (deleted > 0) messageParts.add("deleted $deleted")
        val summary = if (messageParts.isEmpty()) {
            "No changes detected."
        } else {
            messageParts.joinToString(", ", prefix = "Parameters: ")
        }

        // 🔹 Intent to open ConfigMaster and select updated config
        val intent = Intent(context, ConfigMasterHiltActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            putExtra(ConfigMasterActivity.EXTRA_SELECTED_CONFIG, configName)
        }

        val pendingIntent = PendingIntent.getActivity(
            context,
            configName.hashCode(), // unique per config
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(context, CONFIG_CHANGES_CHANNEL)
            .setSmallIcon(R.drawable.stat_notify_sync)
            .setContentTitle("Config \"$configName\" updated")
            .setContentText(summary)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .build()

        // Permission check for Android 13+
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU ||
            ContextCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS)
            == PackageManager.PERMISSION_GRANTED
        ) {
            NotificationManagerCompat.from(context).notify(
                System.currentTimeMillis().toInt(),
                notification
            )
        }
    }
}