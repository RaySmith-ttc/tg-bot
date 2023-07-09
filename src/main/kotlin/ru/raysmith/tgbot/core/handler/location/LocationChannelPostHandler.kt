package ru.raysmith.tgbot.core.handler.location

import ru.raysmith.tgbot.core.Bot
import ru.raysmith.tgbot.core.BotContext
import ru.raysmith.tgbot.core.HandlerDsl
import ru.raysmith.tgbot.core.LocationHandler
import ru.raysmith.tgbot.core.handler.ChannelPostHandler
import ru.raysmith.tgbot.model.network.updates.Update
import ru.raysmith.tgbot.network.TelegramFileService
import ru.raysmith.tgbot.network.TelegramService
import ru.raysmith.tgbot.utils.locations.LocationConfig
import ru.raysmith.tgbot.utils.locations.LocationsWrapper

data class LocationChannelPostHandlerData<T : LocationConfig>(
    val handler: (context(T) LocationChannelPostHandler<T>.() -> Unit)? = null
)

@HandlerDsl
open class LocationChannelPostHandler<T : LocationConfig>(
    override val update: Update, service: TelegramService, fileService: TelegramFileService,
    private val handlerData: Map<String, LocationChannelPostHandlerData<T>>,
    override val locationsWrapper: LocationsWrapper<T>
) : ChannelPostHandler(update.message!!, service, fileService), LocationHandler<T> {
    
    override val config by lazy { config() }
    override suspend fun handle() {
        handlerData.forEach {
            it.value.handler?.let { it1 -> it1(config, this) }
        }
    }
    override fun <R> withBot(bot: Bot, block: BotContext<ChannelPostHandler>.() -> R): R {
        return LocationChannelPostHandler(update, bot.service, bot.fileService, handlerData, locationsWrapper).block()
    }
}