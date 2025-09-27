package com.spascoding.configmastersdk.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.spascoding.configmastersdk.domain.models.ConfigEntity

@Database(entities = [ConfigEntity::class], version = 1)
abstract class ConfigDatabase : RoomDatabase() {
    abstract fun configDao(): ConfigDao
}