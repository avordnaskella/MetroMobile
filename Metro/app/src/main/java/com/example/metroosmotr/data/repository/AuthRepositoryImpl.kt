package com.example.metroosmotr.data.repository

import com.example.metroosmotr.domain.repository.AuthRepository
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor() : AuthRepository {

    //  здесь будет вызов Retrofit)
    override suspend fun login(login: String, password: String) {
        // имитируем задержку сети
        kotlinx.coroutines.delay(1000)

    }

    override suspend fun logout() {

    }

    override suspend fun getCurrentUser(): String? {
        return "ivanov" // Заглушка
    }
}