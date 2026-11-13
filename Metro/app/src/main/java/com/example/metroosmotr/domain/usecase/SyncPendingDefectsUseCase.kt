package com.example.metroosmotr.domain.usecase

import com.example.metroosmotr.domain.repository.DefectSyncSource

class SyncPendingDefectsUseCase(
    private val syncSource: DefectSyncSource,
) {
    suspend operator fun invoke(): Result<Unit> =
        try {
            val unsynced = syncSource.getUnsyncedDefects()
            for (defect in unsynced) {
                syncSource.pushToServer(defect)
                syncSource.markAsSynced(defect.id)
            }
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
}
