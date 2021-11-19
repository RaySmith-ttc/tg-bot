package ru.raysmith.tgbot.core.handler

import ru.raysmith.tgbot.core.EventHandler
import ru.raysmith.tgbot.model.network.updates.Update

class UnknownEventHandler(val update: Update, val handler: UnknownEventHandler.() -> Unit = {}) : EventHandler {
    override suspend fun handle() = handler()
    override var chatId: String? = update.message?.chat?.id?.toString()
        ?: update.callbackQuery?.message?.chat?.id?.toString()
        // TODO more
}