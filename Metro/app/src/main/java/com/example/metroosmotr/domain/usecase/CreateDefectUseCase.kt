package com.example.metroosmotr.domain.usecase

import com.example.metroosmotr.domain.model.Defect
import com.example.metroosmotr.domain.repository.DefectWriter

class CreateDefectUseCase(
    private val writer: DefectWriter,
    private val validatePicket: ValidatePicketUseCase,
) {
    suspend operator fun invoke(defect: Defect): Result<Unit> {
        // валидация пикета
        validatePicket(defect.sectionId, defect.picket).onFailure {
            return Result.failure(it)
        }

        // вохранение локально со статусом "Ожидает отправки"
        return try {
            writer.saveDefect(defect)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
