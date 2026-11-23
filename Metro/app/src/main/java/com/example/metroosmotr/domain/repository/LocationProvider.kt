package com.example.metroosmotr.domain.repository

interface LocationProvider {
    suspend fun getCurrentLocation(): Pair<Double, Double>?
}