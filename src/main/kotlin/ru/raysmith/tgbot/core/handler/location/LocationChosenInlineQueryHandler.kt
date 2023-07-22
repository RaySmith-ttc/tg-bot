package ru.raysmith.tgbot.core.handler.location

import ru.raysmith.tgbot.core.Bot
import ru.raysmith.tgbot.core.BotContext
import ru.raysmith.tgbot.core.handler.HandlerDsl
import ru.raysmith.tgbot.core.handler.LocationHandler
import ru.raysmith.tgbot.core.handler.base.ChosenInlineQueryHandler
import ru.raysmith.tgbot.model.network.updates.Update
import ru.raysmith.tgbot.network.TelegramFileService
import ru.raysmith.tgbot.network.TelegramService
import ru.raysmith.tgbot.utils.locations.LocationConfig
import ru.raysmith.tgbot.utils.locations.LocationsWrapper

data class LocationChosenInlineQueryHandlerData<T : LocationConfig>(
    val handler: (context(T) LocationChosenInlineQueryHandler<T>.() -> Unit)? = null
)

@HandlerDsl
class LocationChosenInlineQueryHandler<T : LocationConfig>(
    override val update: Update, service: TelegramService, fileService: TelegramFileService,
    private val handlerData: MutableMap<String, LocationChosenInlineQueryHandlerData<T>>,
    override val locationsWrapper: LocationsWrapper<T>
) : ChosenInlineQueryHandler(update.chosenInlineResult!!, service, fileService), LocationHandler<T> {

    override val config by lazy { config() }
    override suspend fun handle() {
        handlerData.forEach {
            it.value.handler?.let { it1 -> it1(config, this) }
        }
    }
    override fun <R> withBot(bot: Bot, block: BotContext<ChosenInlineQueryHandler>.() -> R): R {
        return LocationChosenInlineQueryHandler(update, bot.service, bot.fileService, handlerData, locationsWrapper).let {
            this.block()
        }
    }
}