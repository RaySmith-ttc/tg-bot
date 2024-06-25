package ru.raysmith.tgbot.utils

import ru.raysmith.tgbot.core.handler.EventHandler

/** Represents feature for bot's event handlers */
fun interface BotFeature {
    suspend fun handle(handler: EventHandler, handled: Boolean)
}