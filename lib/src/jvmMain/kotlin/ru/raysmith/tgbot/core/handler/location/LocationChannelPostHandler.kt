package ru.raysmith.tgbot.core.handler.location

import ru.raysmith.tgbot.core.Bot
import ru.raysmith.tgbot.core.BotContext
import ru.raysmith.tgbot.core.handler.LocationHandler
import ru.raysmith.tgbot.core.handler.base.ChannelPostHandler
import ru.raysmith.tgbot.model.network.updates.Update
import ru.raysmith.tgbot.utils.locations.LocationFlowContext
import ru.raysmith.tgbot.utils.locations.LocationsWrapper

data class LocationChannelPostHandlerData<T : LocationFlowContext>(
    val handler: (suspend context(T) LocationChannelPostHandler<T>.() -> Unit)? = null
)

open class LocationChannelPostHandler<T : LocationFlowContext>(
    override val update: Update, bot: Bot,
    private val handlerData: List<LocationChannelPostHandlerData<T>>,
    override val locationsWrapper: LocationsWrapper<T>
) : ChannelPostHandler(update.message!!, bot), LocationHandler<T, ChannelPostHandler> {
    
    override val config by lazy { config() }
    override suspend fun handle() {
        handlerData.forEach {
            it.handler?.let { it1 -> it1(config, this) }?.also {
                handled = true
            }
        }
        handleLocalFeatures(handled)
    }
    override suspend fun <R> withBot(bot: Bot, block: suspend BotContext<ChannelPostHandler>.() -> R): R {
        return LocationChannelPostHandler(update, bot, handlerData, locationsWrapper).block()
    }
}