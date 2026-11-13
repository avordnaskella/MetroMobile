package com.example.metroosmotr.domain.usecase

import com.example.metroosmotr.domain.model.DefectStatus
import com.example.metroosmotr.domain.repository.DefectWriter

class UpdateDefectStatusUseCase(
    private val writer: DefectWriter,
) {
    suspend operator fun invoke(
        defectId: String,
        currentStatus: DefectStatus,
        newStatus: DefectStatus,
    ): Result<Unit> {
        val isValid =
            when (currentStatus) {
                DefectStatus.NEW -> newStatus == DefectStatus.IN_PROGRESS
                DefectStatus.IN_PROGRESS -> newStatus == DefectStatus.FIXED
                DefectStatus.FIXED -> newStatus == DefectStatus.VERIFIED || newStatus == DefectStatus.IN_PROGRESS
                DefectStatus.VERIFIED -> false
            }

        if (!isValid) return Result.failure(IllegalStateException("Недопустимый переход статуса"))

        return try {
            writer.updateStatus(defectId, newStatus)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
