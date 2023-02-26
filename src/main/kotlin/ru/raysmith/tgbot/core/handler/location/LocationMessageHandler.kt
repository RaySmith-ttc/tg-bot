package ru.raysmith.tgbot.core.handler.location

import ru.raysmith.tgbot.core.*
import ru.raysmith.tgbot.core.handler.MessageHandler
import ru.raysmith.tgbot.model.network.updates.Update
import ru.raysmith.tgbot.network.TelegramFileService
import ru.raysmith.tgbot.network.TelegramService
import ru.raysmith.tgbot.utils.locations.LocationConfig
import ru.raysmith.tgbot.utils.locations.LocationsWrapper

@HandlerDsl
open class LocationMessageHandler<T : LocationConfig>(
    override val update: Update, service: TelegramService, fileService: TelegramFileService,
    private val handlerData: Map<String, LocationMessageHandlerData<T>>,
    override val locationsWrapper: LocationsWrapper<T>
) : MessageHandler(update.message!!, service, fileService), LocationHandler<T> {
    
    override fun toLocation(name: String) = defaultToLocation(name)
    override suspend fun handle() {
        handlerData.forEach {
            it.value.handler?.let { it1 -> it1(config()) }
        }
    }
    override fun withBot(bot: Bot, block: BotContext<MessageHandler>.() -> Any) {
        LocationMessageHandler(update, service, fileService, handlerData, locationsWrapper).apply {
            this.block()
        }
    }
}