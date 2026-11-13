package com.example.metroosmotr.domain.repository

import com.example.metroosmotr.domain.model.Defect
import com.example.metroosmotr.domain.model.DefectStatus

interface DefectWriter {
    suspend fun saveDefect(defect: Defect)

    suspend fun updateStatus(
        id: String,
        newStatus: DefectStatus,
    )

    suspend fun attachPhoto(
        id: String,
        photoPath: String,
    )
}
