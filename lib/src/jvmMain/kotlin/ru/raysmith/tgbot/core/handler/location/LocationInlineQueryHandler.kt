package ru.raysmith.tgbot.core.handler.location

import ru.raysmith.tgbot.core.Bot
import ru.raysmith.tgbot.core.BotContext
import ru.raysmith.tgbot.core.handler.LocationHandler
import ru.raysmith.tgbot.core.handler.base.InlineQueryHandler
import ru.raysmith.tgbot.model.network.updates.Update
import ru.raysmith.tgbot.utils.locations.LocationConfig
import ru.raysmith.tgbot.utils.locations.LocationsWrapper

data class LocationInlineQueryHandlerData<T : LocationConfig>(
    val handler: (suspend context(T) LocationInlineQueryHandler<T>.() -> Unit)? = null
)

class LocationInlineQueryHandler<T : LocationConfig>(
    override val update: Update, bot: Bot,
    private val handlerData: MutableList<LocationInlineQueryHandlerData<T>>,
    override val locationsWrapper: LocationsWrapper<T>
) : InlineQueryHandler(update.inlineQuery!!, bot), LocationHandler<T> {

    override val config by lazy { config() }
    override suspend fun handle() {
        handlerData.forEach {
            it.handler?.let { it1 -> it1(config, this) }?.also {
                handled = true
            }
        }
        handleLocalFeatures(handled)
    }
    override suspend fun <R> withBot(bot: Bot, block: suspend BotContext<InlineQueryHandler>.() -> R): R {
        return LocationInlineQueryHandler(update, bot, handlerData, locationsWrapper).block()
    }
}