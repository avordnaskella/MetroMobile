package com.example.metroosmotr.presentation.defect

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.metroosmotr.domain.model.Defect
import com.example.metroosmotr.domain.usecase.GetDefectUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DefectViewModel @Inject constructor(
    private val getDefectUseCase: GetDefectUseCase
) : ViewModel() {

    // Состояние хранит либо Defect (если загружено), либо null (если грузится или нет данных)
    private val _uiState = MutableStateFlow<Defect?>(null)
    val uiState: StateFlow<Defect?> = _uiState.asStateFlow()

    fun loadDefect(id: String) {
        viewModelScope.launch {
            val result = getDefectUseCase(id)
            if (result.isSuccess) {
                _uiState.value = result.getOrNull()
            }
        }
    }
}