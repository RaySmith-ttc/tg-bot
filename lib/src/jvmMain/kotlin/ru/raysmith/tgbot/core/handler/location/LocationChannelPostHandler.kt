package ru.raysmith.tgbot.core.handler.location

import ru.raysmith.tgbot.core.Bot
import ru.raysmith.tgbot.core.BotContext
import ru.raysmith.tgbot.core.handler.LocationHandler
import ru.raysmith.tgbot.core.handler.base.ChannelPostHandler
import ru.raysmith.tgbot.model.network.updates.Update
import ru.raysmith.tgbot.utils.locations.LocationFlowContext
import ru.raysmith.tgbot.utils.locations.LocationsWrapper

data class LocationChannelPostHandlerData<LFC : LocationFlowContext>(
    val handler: (suspend context(LFC) LocationChannelPostHandler<LFC>.() -> Unit)? = null
)

open class LocationChannelPostHandler<LFC : LocationFlowContext>(
    override val update: Update, bot: Bot,
    private val handlerData: List<LocationChannelPostHandlerData<LFC>>,
    override val locationsWrapper: LocationsWrapper<LFC>
) : ChannelPostHandler(update.message!!, bot), LocationHandler<LFC, ChannelPostHandler> {
    
    override val locationFlowContext by lazy { locationFlowContext() }
    override suspend fun handle() {
        handlerData.forEach {
            it.handler?.let { it1 -> it1(locationFlowContext, this) }?.also {
                handled = true
            }
        }
        handleLocalFeatures(handled)
    }
    override suspend fun <R> withBot(bot: Bot, block: suspend BotContext<ChannelPostHandler>.() -> R): R {
        return LocationChannelPostHandler(update, bot, handlerData, locationsWrapper).block()
    }
}