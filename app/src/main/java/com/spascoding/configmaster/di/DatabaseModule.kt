package com.spascoding.configmaster.di

import android.content.Context
import androidx.room.Room
import com.spascoding.configmaster.data.database.ConfigDao
import com.spascoding.configmaster.data.database.ConfigDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): ConfigDatabase {
        return Room.databaseBuilder(
            context,
            ConfigDatabase::class.java,
            "config_database"
        ).fallbackToDestructiveMigration().build()
    }

    @Provides
    fun provideConfigDao(database: ConfigDatabase): ConfigDao {
        return database.configDao()
    }

}