package com.spascoding.configmaster.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.spascoding.configmaster.domain.models.ConfigEntity

@Dao
interface ConfigDao {

    @Query("SELECT * FROM configurations WHERE appId = :appId")
    suspend fun getConfig(appId: String): List<ConfigEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertConfig(configList: List<ConfigEntity>)

}