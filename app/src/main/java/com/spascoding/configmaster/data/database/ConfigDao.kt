package com.spascoding.configmaster.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.spascoding.configmaster.domain.models.ConfigEntity

@Dao
interface ConfigDao {

    @Query("SELECT * FROM configurations WHERE appId = :appId")
    suspend fun getConfig(appId: String): List<ConfigEntity>

    @Query("SELECT * FROM configurations")
    suspend fun getAllConfigs(): List<ConfigEntity>

    @Query("SELECT DISTINCT appId FROM configurations")
    suspend fun getAllAppIds(): List<String>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveConfig(config: List<ConfigEntity>)

    @Update
    suspend fun updateConfig(config: ConfigEntity)

    @Query("DELETE FROM configurations WHERE appId = :appId")
    suspend fun deleteConfig(appId: String)

}