package ru.raysmith.tgbot.core

import ru.raysmith.tgbot.network.TelegramService

interface ApiCaller {
    val service: TelegramService
}