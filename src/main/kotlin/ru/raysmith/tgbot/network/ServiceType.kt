package ru.raysmith.tgbot.network

// TODO set internal
enum class ServiceType {
    DEFAULT, FILE;

    fun getBaseUrl(token: String) = when(this) {
        DEFAULT -> "${TelegramApi2.BASE_URL}/bot$token/"
        FILE -> "${TelegramApi2.BASE_URL}/file/bot$token/"
    }
}