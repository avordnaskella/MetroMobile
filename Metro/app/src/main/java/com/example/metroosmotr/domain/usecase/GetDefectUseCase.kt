package com.example.metroosmotr.domain.usecase

import com.example.metroosmotr.domain.model.Defect
import com.example.metroosmotr.domain.repository.DefectReader

class GetDefectUseCase(
    private val reader: DefectReader,
) {
    suspend operator fun invoke(id: String): Result<Defect> {
        return try {
            val defect =
                reader.getDefectById(id)
                    ?: return Result.failure(NoSuchElementException("Неисправность не найдена"))
            Result.success(defect)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
