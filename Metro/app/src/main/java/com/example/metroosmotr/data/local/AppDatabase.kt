package com.example.metroosmotr.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.metroosmotr.data.local.dao.DefectDao
import com.example.metroosmotr.data.local.entity.DefectEntity

@Database(entities = [DefectEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun defectDao(): DefectDao
}