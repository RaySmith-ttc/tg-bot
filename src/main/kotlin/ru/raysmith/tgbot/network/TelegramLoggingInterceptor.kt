package ru.raysmith.tgbot.network

import okhttp3.logging.HttpLoggingInterceptor

object TelegramLoggingInterceptor : HttpLoggingInterceptor.Logger {
    override fun log(message: String) {
        TelegramApi.logger.debug(message)
    }

}