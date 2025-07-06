package ru.raysmith.tgbot.core.handler.location

import ru.raysmith.tgbot.core.Bot
import ru.raysmith.tgbot.core.BotContext
import ru.raysmith.tgbot.core.handler.LocationHandler
import ru.raysmith.tgbot.core.handler.base.MessageReactionCountHandler
import ru.raysmith.tgbot.model.network.updates.Update
import ru.raysmith.tgbot.utils.locations.LocationFlowContext
import ru.raysmith.tgbot.utils.locations.LocationsWrapper

data class LocationMessageReactionCountHandlerData<LFC : LocationFlowContext>(
    val handler: (suspend context(LFC) LocationMessageReactionCountHandler<LFC>.() -> Unit)? = null
)

class LocationMessageReactionCountHandler<LFC : LocationFlowContext>(
    override val update: Update, bot: Bot,
    private val handlerData: MutableList<LocationMessageReactionCountHandlerData<LFC>>,
    override val locationsWrapper: LocationsWrapper<LFC>
) : MessageReactionCountHandler(update.messageReactionCount!!, bot), LocationHandler<LFC, MessageReactionCountHandler> {

    override val locationFlowContext by lazy { locationFlowContext() }
    override suspend fun handle() {
        handlerData.forEach {
            it.handler?.let { it1 -> it1(locationFlowContext, this) }?.also {
                handled = true
            }
        }
        handleLocalFeatures(handled)
    }
    override suspend fun <R> withBot(bot: Bot, block: suspend BotContext<MessageReactionCountHandler>.() -> R): R {
        return LocationMessageReactionCountHandler(update, bot, handlerData, locationsWrapper).block()
    }
}