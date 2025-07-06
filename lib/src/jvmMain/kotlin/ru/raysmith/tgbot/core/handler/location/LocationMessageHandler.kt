package ru.raysmith.tgbot.core.handler.location

import ru.raysmith.tgbot.core.Bot
import ru.raysmith.tgbot.core.BotContext
import ru.raysmith.tgbot.core.handler.LocationHandler
import ru.raysmith.tgbot.core.handler.base.MessageHandler
import ru.raysmith.tgbot.model.network.updates.Update
import ru.raysmith.tgbot.utils.locations.LocationFlowContext
import ru.raysmith.tgbot.utils.locations.LocationsWrapper

data class LocationMessageHandlerData<LFC : LocationFlowContext>(
    val handler: (suspend (context(LFC) LocationMessageHandler<LFC>.() -> Unit))? = null
)

open class LocationMessageHandler<LFC : LocationFlowContext>(
    override val update: Update, bot: Bot,
    private val handlerData: List<LocationMessageHandlerData<LFC>>,
    override val locationsWrapper: LocationsWrapper<LFC>
) : MessageHandler(update.message!!, bot), LocationHandler<LFC, MessageHandler> {
    
    override val locationFlowContext by lazy { locationFlowContext() }
    override suspend fun handle() {
        handlerData.forEach {
            it.handler?.let { it1 -> it1(locationFlowContext, this) }?.also {
                handled = true
            }
        }
        handleLocalFeatures(handled)
    }
    override suspend fun <R> withBot(bot: Bot, block: suspend BotContext<MessageHandler>.() -> R): R {
        return LocationMessageHandler(update, bot, handlerData, locationsWrapper).block()
    }
}

