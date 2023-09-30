package ru.raysmith.tgbot.network

import io.ktor.client.request.*
import okhttp3.Request
import ru.raysmith.tgbot.model.network.Error

class TelegramApiException(val error: Error, val request: Request) : Exception(error.description) {
    override fun toString(): String {
        return "Code: ${error.errorCode}, Message: ${error.description}"
    }
}

class TelegramApiException2(val error: Error, val request: HttpRequest) : Exception(error.description) {
    override fun toString(): String {
        return "Code: ${error.errorCode}, Message: ${error.description}"
    }
}