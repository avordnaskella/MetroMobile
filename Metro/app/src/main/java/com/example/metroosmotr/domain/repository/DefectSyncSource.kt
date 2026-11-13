package com.example.metroosmotr.domain.repository

import com.example.metroosmotr.domain.model.Defect

interface DefectSyncSource {
    suspend fun fetchFromServer(): List<Defect>

    suspend fun getUnsyncedDefects(): List<Defect>

    suspend fun pushToServer(defect: Defect): Defect

    suspend fun markAsSynced(id: String)
}
