package com.example.metroosmotr.di

import android.content.Context
import androidx.room.Room
import com.example.metroosmotr.data.local.AppDatabase
import com.example.metroosmotr.data.local.UserPreferences
import com.example.metroosmotr.data.local.dao.DefectDao
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
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "metro_database"
        ).build()
    }

    @Provides
    fun provideDefectDao(db: AppDatabase): DefectDao {
        return db.defectDao()
    }
}