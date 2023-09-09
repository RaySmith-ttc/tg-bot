package ru.raysmith.tgbot.network

import okhttp3.logging.HttpLoggingInterceptor
import java.net.URLDecoder
import java.nio.charset.StandardCharsets

object TelegramLoggingInterceptor : HttpLoggingInterceptor.Logger {
    override fun log(message: String) {
        try {
            TelegramApi.logger.debug(URLDecoder.decode(message, StandardCharsets.UTF_8.toString()).replace("\n", "\\n"))
        } catch (e: Exception) {
            TelegramApi.logger.debug(message)
        }
    }
}