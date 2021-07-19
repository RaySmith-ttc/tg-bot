package ru.raysmith.tgbot.core

import ru.raysmith.tgbot.model.network.updates.Update

class UnknownEventHandler(val update: Update, val handler: UnknownEventHandler.() -> Unit = {}) : EventHandler {
    override suspend fun handle() = handler()
}