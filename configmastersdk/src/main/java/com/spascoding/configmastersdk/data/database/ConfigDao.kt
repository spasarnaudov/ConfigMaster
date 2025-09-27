package com.spascoding.configmastersdk.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.spascoding.configmastersdk.domain.models.ConfigEntity

@Dao
interface ConfigDao {

    @Query("SELECT DISTINCT name FROM configurations")
    suspend fun getAllConfigNames(): List<String>

    @Query("SELECT * FROM configurations WHERE name = :configName")
    suspend fun getConfig(configName: String): List<ConfigEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertConfig(configList: List<ConfigEntity>)

    @Query("DELETE FROM configurations WHERE name = :configName AND parameter IN (:parameterNames)")
    suspend fun deleteConfig(configName: String, parameterNames: List<String>)

    @Query("DELETE FROM configurations WHERE name = :configName")
    suspend fun deleteConfig(configName: String)
}