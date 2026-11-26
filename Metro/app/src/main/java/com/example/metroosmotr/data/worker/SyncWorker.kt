package com.example.metroosmotr.data.worker

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.metroosmotr.domain.usecase.SyncPendingDefectsUseCase
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

@HiltWorker
class SyncWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted params: WorkerParameters,
    private val syncPendingDefects: SyncPendingDefectsUseCase
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        return try {
            // Пытаемся синхронизировать неотправленные данные
            val result = syncPendingDefects()

            if (result.isSuccess) {
                // Если успешно - задача выполнена
                Result.success()
            } else {
                // Если ошибка сети - попробуем позже (WorkManager перезапустит)
                Result.retry()
            }
        } catch (e: Exception) {
            Result.retry()
        }
    }
}