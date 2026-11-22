package com.example.metroosmotr.domain.usecase

import com.example.metroosmotr.domain.repository.DefectSyncSource
import javax.inject.Inject
class RefreshDefectUseCase @Inject constructor(
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
