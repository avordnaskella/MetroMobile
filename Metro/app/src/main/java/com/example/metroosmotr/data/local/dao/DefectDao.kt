package com.example.metroosmotr.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.metroosmotr.data.local.entity.DefectEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface DefectDao {

    // Flow автоматически обновляет UI, когда данные в БД меняются
    @Query("SELECT * FROM defects ORDER BY detectedAt DESC")
    fun observeAll(): Flow<List<DefectEntity>>

    @Query("SELECT * FROM defects WHERE id = :id")
    suspend fun getById(id: String): DefectEntity?

    @Query("SELECT * FROM defects WHERE isSynced = 0")
    suspend fun getUnsynced(): List<DefectEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(defect: DefectEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(defects: List<DefectEntity>)

    @Update
    suspend fun update(defect: DefectEntity)

    @Query("UPDATE defects SET isSynced = 1 WHERE id = :id")
    suspend fun markAsSynced(id: String)

    @Query("DELETE FROM defects")
    suspend fun clearAll()
}