package com.spascoding.configmastersdk.domain.models

import androidx.room.Entity

@Entity(tableName = "configurations", primaryKeys = ["name", "parameter"])
data class ConfigEntity(
    val name: String,
    val parameter: String,
    val originalValue: String,
    val modifiedValue: String = "",
)