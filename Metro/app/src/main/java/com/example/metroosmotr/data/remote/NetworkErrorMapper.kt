package com.example.metroosmotr.data.remote

import com.example.metroosmotr.domain.model.AppError
import java.io.IOException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import retrofit2.HttpException

object NetworkErrorMapper {
    fun map(throwable: Throwable): AppError {
        return when (throwable) {
            is UnknownHostException -> AppError.Network()
            is SocketTimeoutException -> AppError.Timeout()
            is HttpException -> {
                when (throwable.code()) {
                    401 -> AppError.Unauthorized()
                    500, 502, 503 -> AppError.Server()
                    else -> AppError.Unknown("HTTP код: ${throwable.code()}")
                }
            }
            is IOException -> AppError.Network()
            else -> AppError.Unknown(throwable.message ?: "Неизвестная ошибка")
        }
    }
}