package ru.raysmith.tgbot.core.handler

import org.slf4j.LoggerFactory
import ru.raysmith.tgbot.model.network.updates.Update
import ru.raysmith.tgbot.model.network.updates.UpdateType

@HandlerDsl
interface EventHandlerFactory {
    companion object {
        @Deprecated("Use main logger", ReplaceWith("Bot.logger"))
        internal val logger = LoggerFactory.getLogger("event-handler-factory")
    }
    
    val allowedUpdates: Set<UpdateType>
    
    fun getHandler(update: Update): EventHandler

    fun clear()
}