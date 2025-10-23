package com.spascoding.configmastersdk.domain.models

data class ConfigUpdateResult(
    val inserted: Int,
    val updated: Int,
    val deleted: Int
) {
    val hasChanges: Boolean
        get() = inserted > 0 || updated > 0 || deleted > 0
}