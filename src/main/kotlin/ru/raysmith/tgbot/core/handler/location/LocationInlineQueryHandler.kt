package ru.raysmith.tgbot.core.handler.location

import ru.raysmith.tgbot.core.Bot
import ru.raysmith.tgbot.core.BotContext
import ru.raysmith.tgbot.core.HandlerDsl
import ru.raysmith.tgbot.core.LocationHandler
import ru.raysmith.tgbot.core.handler.InlineQueryHandler
import ru.raysmith.tgbot.model.network.updates.Update
import ru.raysmith.tgbot.network.TelegramFileService
import ru.raysmith.tgbot.network.TelegramService
import ru.raysmith.tgbot.utils.locations.LocationConfig
import ru.raysmith.tgbot.utils.locations.LocationsWrapper

data class LocationInlineQueryHandlerData<T : LocationConfig>(
    val handler: (context(T) LocationInlineQueryHandler<T>.() -> Unit)? = null
)

@HandlerDsl
class LocationInlineQueryHandler<T : LocationConfig>(
    override val update: Update, service: TelegramService, fileService: TelegramFileService,
    private val handlerData: MutableMap<String, LocationInlineQueryHandlerData<T>>,
    override val locationsWrapper: LocationsWrapper<T>
) : InlineQueryHandler(update.inlineQuery!!, service, fileService), LocationHandler<T> {

    override val config by lazy { config() }
    override suspend fun handle() {
        handlerData.forEach {
            it.value.handler?.let { it1 -> it1(config, this) }
        }
    }
    override fun <R> withBot(bot: Bot, block: BotContext<InlineQueryHandler>.() -> R): R {
        return LocationInlineQueryHandler(update, bot.service, bot.fileService, handlerData, locationsWrapper).block()
    }
}