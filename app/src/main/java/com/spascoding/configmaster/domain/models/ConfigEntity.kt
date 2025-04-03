package com.spascoding.configmaster.domain.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "configurations")
data class ConfigEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val appId: String,
    val key: String,
    val originalValue: String,
    val modifiedValue: String = "",
)