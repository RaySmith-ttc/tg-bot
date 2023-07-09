package ru.raysmith.tgbot.core

import org.slf4j.LoggerFactory
import ru.raysmith.tgbot.model.network.updates.Update
import ru.raysmith.tgbot.model.network.updates.UpdateType
import ru.raysmith.tgbot.network.TelegramFileService
import ru.raysmith.tgbot.network.TelegramService

@HandlerDsl
interface EventHandlerFactory {
    companion object {
        @Deprecated("Use main logger", ReplaceWith("Bot.logger"))
        internal val logger = LoggerFactory.getLogger("event-handler-factory")
    }
    
    val allowedUpdates: Set<UpdateType>
    
    fun getHandler(update: Update, service: TelegramService, fileService: TelegramFileService): EventHandler
}