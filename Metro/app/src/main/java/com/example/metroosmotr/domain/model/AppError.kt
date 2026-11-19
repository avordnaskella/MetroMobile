package com.example.metroosmotr.domain.model

sealed class AppError(override val message: String) : Exception(message) {
    class Network(override val message: String = "Нет подключения к интернету") : AppError(message)
    class Timeout(override val message: String = "Сервер долго не отвечает") : AppError(message)
    class Unauthorized(override val message: String = "Сессия истекла, войдите снова") : AppError(message)
    class Server(override val message: String = "Ошибка сервера. Попробуйте позже") : AppError(message)
    class Unknown(override val message: String) : AppError(message)
}