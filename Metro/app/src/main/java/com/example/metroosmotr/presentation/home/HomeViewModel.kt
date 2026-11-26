package com.example.metroosmotr.presentation.home

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.example.metroosmotr.data.worker.SyncWorker
import com.example.metroosmotr.domain.model.DefectStatus
import com.example.metroosmotr.domain.model.Severity
import com.example.metroosmotr.domain.usecase.ObserveDefectsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val observeDefects: ObserveDefectsUseCase,
    @ApplicationContext private val context: Context // Контекст для WorkManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        loadData()
        startBackgroundSync() // Запускаем фоновую синхронизацию
    }

    private fun loadData() {
        viewModelScope.launch {
            // Подписываемся на базу данных Room (Offline-First)
            observeDefects().collect { defects ->
                val unsynced = defects.count { it.status == DefectStatus.NEW } // В реальности тут проверка на isSynced
                val critical = defects.count { it.severity == Severity.CRITICAL }

                _uiState.update {
                    it.copy(
                        isLoading = false,
                        unsyncedCount = unsynced,
                        criticalCount = critical
                    )
                }
            }
        }
    }

    // Настройка WorkManager
    private fun startBackgroundSync() {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED) // Только при наличии сети
            .build()

        // Задача раз в 15 минут
        val syncRequest = PeriodicWorkRequestBuilder<SyncWorker>(15, TimeUnit.MINUTES)
            .setConstraints(constraints)
            .build()

        WorkManager.getInstance(context).enqueueUniquePeriodicWork(
            "sync_defects",
            ExistingPeriodicWorkPolicy.KEEP, // Не создавать дубликаты
            syncRequest
        )
    }
}