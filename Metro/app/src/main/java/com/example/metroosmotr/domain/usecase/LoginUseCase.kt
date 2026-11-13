package com.example.metroosmotr.domain.usecase

import com.example.metroosmotr.domain.repository.AuthRepository

class LoginUseCase(
    private val authRepository: AuthRepository,
) {
    suspend operator fun invoke(
        login: String,
        password: String,
    ): Result<Unit> =
        try {
            authRepository.login(login, password)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
}
