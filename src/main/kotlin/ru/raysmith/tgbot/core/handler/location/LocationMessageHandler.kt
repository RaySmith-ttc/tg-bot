package ru.raysmith.tgbot.core.handler.location

import ru.raysmith.tgbot.core.*
import ru.raysmith.tgbot.core.handler.HandlerDsl
import ru.raysmith.tgbot.core.handler.LocationHandler
import ru.raysmith.tgbot.core.handler.base.MessageHandler
import ru.raysmith.tgbot.model.network.updates.Update
import ru.raysmith.tgbot.network.TelegramFileService
import ru.raysmith.tgbot.network.TelegramService
import ru.raysmith.tgbot.utils.locations.LocationConfig
import ru.raysmith.tgbot.utils.locations.LocationsWrapper

data class LocationMessageHandlerData<T : LocationConfig>(
    val handler: (context(T) LocationMessageHandler<T>.() -> Unit)? = null
)

@HandlerDsl
open class LocationMessageHandler<T : LocationConfig>(
    override val update: Update, service: TelegramService, fileService: TelegramFileService,
    private val handlerData: Map<String, LocationMessageHandlerData<T>>,
    override val locationsWrapper: LocationsWrapper<T>
) : MessageHandler(update.message!!, service, fileService), LocationHandler<T> {
    
    override val config by lazy { config() }
    override suspend fun handle() {
        handlerData.forEach {
            it.value.handler?.let { it1 -> it1(config, this) }
        }
    }
    override fun <R> withBot(bot: Bot, block: BotContext<MessageHandler>.() -> R): R {
        return LocationMessageHandler(update, bot.service, bot.fileService, handlerData, locationsWrapper).block()
    }
}

