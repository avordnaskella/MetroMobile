package com.example.metroosmotr.domain.usecase

import com.example.metroosmotr.domain.repository.DefectWriter

class AttachPhotoUseCase(
    private val writer: DefectWriter,
) {
    suspend operator fun invoke(
        defectId: String,
        photoPath: String,
    ): Result<Unit> =
        try {
            writer.attachPhoto(defectId, photoPath)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
}
