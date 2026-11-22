package com.example.metroosmotr.presentation.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.metroosmotr.domain.model.AppError
import com.example.metroosmotr.domain.usecase.ObserveDefectsUseCase
import com.example.metroosmotr.domain.usecase.RefreshDefectUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ListViewModel @Inject constructor(
    private val observeDefects: ObserveDefectsUseCase,
    private val refreshDefects: RefreshDefectUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(ListUiState())
    val uiState: StateFlow<ListUiState> = _uiState.asStateFlow()

    init {
        loadDefects()
    }

    private fun loadDefects() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            // 1. Сначала слушаем базу данных (Offline-First)
            observeDefects()
                .catch { e ->
                    _uiState.update { it.copy(isLoading = false, error = e.message) }
                }
                .collect { defects ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            defects = defects,
                            error = null
                        )
                    }
                }
        }

        // 2. В фоне пытаемся обновить данные с сервера
        refreshFromServer()
    }

    fun refreshFromServer() {
        viewModelScope.launch {
            val result = refreshDefects()
            if (result.isFailure) {
                val error = result.exceptionOrNull()
                if (error is AppError.Network) {
                    _uiState.update { it.copy(isOffline = true) }
                }
            } else {
                _uiState.update { it.copy(isOffline = false) }
            }
        }
    }
}