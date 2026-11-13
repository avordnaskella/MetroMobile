package com.example.metroosmotr.domain.usecase

import com.example.metroosmotr.domain.repository.ReferenceRepository

class ValidatePicketUseCase(
    private val referenceRepository: ReferenceRepository,
) {
    suspend operator fun invoke(
        sectionId: String,
        picket: Int,
    ): Result<Unit> {
        return try {
            val section =
                referenceRepository.getSections().find { it.id == sectionId }
                    ?: return Result.failure(IllegalArgumentException("Участок не найден"))

            if (picket in section.start..section.end) {
                Result.success(Unit)
            } else {
                Result.failure(IllegalArgumentException("Пикет вне границ (${section.start}..${section.end})"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
