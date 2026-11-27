package com.example.metroosmotr.presentation.create

import com.example.metroosmotr.domain.model.Severity

data class CreateUiState(
    val picket: String = "",
    val meters: String = "",
    val comment: String = "",
    val gpsCoordinates: String = "",
    val severity: Severity = Severity.LOW,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val isSuccess: Boolean = false,
    val date: String = ""
)