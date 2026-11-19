package com.example.metroosmotr.domain.repository

interface NetworkChecker {
    fun isNetworkAvailable(): Boolean
}