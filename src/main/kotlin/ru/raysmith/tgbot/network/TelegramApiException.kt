package ru.raysmith.tgbot.network

import okhttp3.Request
import ru.raysmith.tgbot.model.network.Error

class TelegramApiException(val error: Error, val request: Request) : Exception(error.description) {
    override fun toString(): String {
        return "Code: ${error.errorCode}, Message: ${error.description}"
    }
}