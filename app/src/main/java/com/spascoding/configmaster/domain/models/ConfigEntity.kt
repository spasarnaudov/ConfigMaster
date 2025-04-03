package com.spascoding.configmaster.domain.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "configurations", primaryKeys = ["appId", "key"])
data class ConfigEntity(
    val appId: String,
    val key: String,
    val originalValue: String,
    val modifiedValue: String = "",
)