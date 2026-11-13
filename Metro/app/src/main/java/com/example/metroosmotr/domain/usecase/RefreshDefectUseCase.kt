package com.example.metroosmotr.domain.usecase

import com.example.metroosmotr.domain.repository.DefectSyncSource

class RefreshDefectUseCase(
    private val syncSource: DefectSyncSource,
) {
    suspend operator fun invoke(): Result<Unit> =
        try {
            val remoteDefects = syncSource.fetchFromServer()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
}
