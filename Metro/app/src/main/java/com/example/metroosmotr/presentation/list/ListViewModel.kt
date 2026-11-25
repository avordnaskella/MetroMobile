package com.example.metroosmotr.presentation.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.metroosmotr.domain.model.AppError
import com.example.metroosmotr.domain.model.DefectStatus
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
            observeDefects()
                .collect { defects ->
                    // Сохраняем полный список
                    _uiState.update { it.copy(allDefects = defects) }
                    // Применяем текущие фильтры
                    applyFilters()
                }
        }
        refreshFromServer()
    }

    fun onSearchQueryChange(query: String) {
        _uiState.update { it.copy(searchQuery = query) }
        applyFilters()
    }

    fun onStatusFilterChange(status: DefectStatus?) {
        _uiState.update { it.copy(selectedStatus = status) }
        applyFilters()
    }

    private fun applyFilters() {
        val state = _uiState.value
        var filtered = state.allDefects

        // Фильтр по тексту (пикет или комментарий)
        if (state.searchQuery.isNotBlank()) {
            filtered = filtered.filter {
                it.comment?.contains(state.searchQuery, ignoreCase = true) == true ||
                        it.picket.toString().contains(state.searchQuery)
            }
        }

        // Фильтр по статусу
        if (state.selectedStatus != null) {
            filtered = filtered.filter { it.status == state.selectedStatus }
        }

        _uiState.update { it.copy(defects = filtered, isLoading = false) }
    }

    fun refreshFromServer() {
        viewModelScope.launch {
            val result = refreshDefects()
            if (result.isFailure) {
                _uiState.update { it.copy(isOffline = true) }
            } else {
                _uiState.update { it.copy(isOffline = false) }
            }
        }
    }
}