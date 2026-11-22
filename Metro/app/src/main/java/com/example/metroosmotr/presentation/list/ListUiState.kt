package com.example.metroosmotr.presentation.list

import com.example.metroosmotr.domain.model.Defect

data class ListUiState(
    val isLoading: Boolean = false,
    val defects: List<Defect> = emptyList(),
    val error: String? = null,
    val isOffline: Boolean = false // показывать ли баннер "Нет подключения"
)