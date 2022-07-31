package ru.raysmith.tgbot.network

// TODO set internal
enum class ServiceType {
    DEFAULT, FILE;

    fun getBaseUrl(token: String) = when(this) {
        DEFAULT -> "${TelegramApi.BASE_URL}/bot$token/"
        FILE -> "${TelegramApi.BASE_URL}/file/bot$token/"
    }
}