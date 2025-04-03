package com.spascoding.configmaster.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.spascoding.configmaster.domain.models.ConfigEntity

@Database(entities = [ConfigEntity::class], version = 1)
abstract class ConfigDatabase : RoomDatabase() {
    abstract fun configDao(): ConfigDao
}