package com.example.metroosmotr.presentation.create

import com.example.metroosmotr.domain.model.Severity

data class CreateUiState(
    val sectionId: String = "",
    val trackId: String = "",
    val picket: String = "",
    val categoryId: Int = 0,
    val typeId: Int = 0,
    val severity: Severity = Severity.LOW,
    val comment: String = "",
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val isSuccess: Boolean = false
)