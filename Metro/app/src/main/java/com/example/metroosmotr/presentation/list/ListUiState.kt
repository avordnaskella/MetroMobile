package com.example.metroosmotr.presentation.list

import com.example.metroosmotr.domain.model.Defect
import com.example.metroosmotr.domain.model.DefectStatus

data class ListUiState(
    val isLoading: Boolean = false,
    val defects: List<Defect> = emptyList(), // Отфильтрованные для показа
    val allDefects: List<Defect> = emptyList(), // Исходные данные
    val error: String? = null,
    val isOffline: Boolean = false,
    val searchQuery: String = "",
    val selectedStatus: DefectStatus? = null // null = показывать все
)