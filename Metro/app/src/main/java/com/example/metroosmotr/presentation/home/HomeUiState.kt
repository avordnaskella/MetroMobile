package com.example.metroosmotr.presentation.home

data class HomeUiState(
    val isLoading: Boolean = false,
    val unsyncedCount: Int = 0,
    val criticalCount: Int = 0,
    val currentShift: String = "Перегон А — Б", // брать из БД
    val error: String? = null
)