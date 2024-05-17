package ru.raysmith.tgbot.utils

import ru.raysmith.tgbot.core.handler.EventHandler

fun interface BotFeature {
    suspend fun handle(handler: EventHandler, handled: Boolean)
}