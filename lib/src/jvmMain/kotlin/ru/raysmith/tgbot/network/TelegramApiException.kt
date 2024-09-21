package ru.raysmith.tgbot.network

import io.ktor.client.request.*
import ru.raysmith.tgbot.model.network.Error

class TelegramApiException(val error: Error, val request: HttpRequest) : Exception(error.description) {
    override fun toString(): String {
        return "Code: ${error.errorCode}, Message: ${error.description}"
    }
}