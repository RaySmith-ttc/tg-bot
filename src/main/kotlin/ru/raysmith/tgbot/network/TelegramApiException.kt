package ru.raysmith.tgbot.network

import ru.raysmith.tgbot.model.network.Error

class TelegramApiException(private val error: Error) : Exception(error.description) {
    override fun toString(): String {
        return "Code: ${error.errorCode}, Message: ${error.description}"
    }
}