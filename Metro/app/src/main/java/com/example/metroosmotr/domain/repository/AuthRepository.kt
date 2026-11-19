package com.example.metroosmotr.domain.repository

interface AuthRepository {
    suspend fun login(
        login: String,
        password: String,
    )

    suspend fun logout()

    suspend fun getCurrentUser(): String? // возвращает id или токен текущего юзера
}
