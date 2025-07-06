package ru.raysmith.tgbot.core.handler.location

import ru.raysmith.tgbot.core.Bot
import ru.raysmith.tgbot.core.BotContext
import ru.raysmith.tgbot.core.handler.LocationHandler
import ru.raysmith.tgbot.core.handler.base.MessageReactionHandler
import ru.raysmith.tgbot.model.network.updates.Update
import ru.raysmith.tgbot.utils.locations.LocationFlowContext
import ru.raysmith.tgbot.utils.locations.LocationsWrapper

data class LocationMessageReactionHandlerData<LFC : LocationFlowContext>(
    val handler: (suspend context(LFC) LocationMessageReactionHandler<LFC>.() -> Unit)? = null
)

class LocationMessageReactionHandler<LFC : LocationFlowContext>(
    override val update: Update, bot: Bot,
    private val handlerData: MutableList<LocationMessageReactionHandlerData<LFC>>,
    override val locationsWrapper: LocationsWrapper<LFC>
) : MessageReactionHandler(update.messageReaction!!, bot), LocationHandler<LFC, MessageReactionHandler> {

    override val locationFlowContext by lazy { locationFlowContext() }
    override suspend fun handle() {
        handlerData.forEach {
            it.handler?.let { it1 -> it1(locationFlowContext, this) }?.also {
                handled = true
            }
        }
        handleLocalFeatures(handled)
    }
    override suspend fun <R> withBot(bot: Bot, block: suspend BotContext<MessageReactionHandler>.() -> R): R {
        return LocationMessageReactionHandler(update, bot, handlerData, locationsWrapper).block()
    }
}