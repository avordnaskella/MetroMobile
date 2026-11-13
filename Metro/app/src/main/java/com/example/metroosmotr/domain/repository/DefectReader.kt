package com.example.metroosmotr.domain.repository

import com.example.metroosmotr.domain.model.Defect
import kotlinx.coroutines.flow.Flow

interface DefectReader {
    fun observeDefects(): Flow<List<Defect>>

    suspend fun getDefectById(id: String): Defect?
}
