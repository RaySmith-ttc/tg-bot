package ru.raysmith.tgbot.core.handler.location

import io.ktor.client.*
import ru.raysmith.tgbot.core.Bot
import ru.raysmith.tgbot.core.BotContext
import ru.raysmith.tgbot.core.handler.HandlerDsl
import ru.raysmith.tgbot.core.handler.LocationHandler
import ru.raysmith.tgbot.core.handler.base.InlineQueryHandler
import ru.raysmith.tgbot.model.network.updates.Update
import ru.raysmith.tgbot.utils.locations.LocationConfig
import ru.raysmith.tgbot.utils.locations.LocationsWrapper

data class LocationInlineQueryHandlerData<T : LocationConfig>(
    val handler: (context(T) LocationInlineQueryHandler<T>.() -> Unit)? = null
)

@HandlerDsl
class LocationInlineQueryHandler<T : LocationConfig>(
    override val update: Update, client: HttpClient,
    private val handlerData: MutableMap<String, LocationInlineQueryHandlerData<T>>,
    override val locationsWrapper: LocationsWrapper<T>
) : InlineQueryHandler(update.inlineQuery!!, client), LocationHandler<T> {

    override val config by lazy { config() }
    override fun handle() {
        handlerData.forEach {
            it.value.handler?.let { it1 -> it1(config, this) }
        }
    }
    override fun <R> withBot(bot: Bot, block: BotContext<InlineQueryHandler>.() -> R): R {
        return LocationInlineQueryHandler(update, bot.client, handlerData, locationsWrapper).block()
    }
}