package ru.raysmith.tgbot.core.handler.location

import ru.raysmith.tgbot.core.Bot
import ru.raysmith.tgbot.core.BotContext
import ru.raysmith.tgbot.core.handler.LocationHandler
import ru.raysmith.tgbot.core.handler.base.MessageHandler
import ru.raysmith.tgbot.model.network.updates.Update
import ru.raysmith.tgbot.utils.locations.LocationFlowContext
import ru.raysmith.tgbot.utils.locations.LocationsWrapper

data class LocationMessageHandlerData<T : LocationFlowContext>(
    val handler: (suspend (context(T) LocationMessageHandler<T>.() -> Unit))? = null
)

open class LocationMessageHandler<T : LocationFlowContext>(
    override val update: Update, bot: Bot,
    private val handlerData: List<LocationMessageHandlerData<T>>,
    override val locationsWrapper: LocationsWrapper<T>
) : MessageHandler(update.message!!, bot), LocationHandler<T, MessageHandler> {
    
    override val config by lazy { config() }
    override suspend fun handle() {
        handlerData.forEach {
            it.handler?.let { it1 -> it1(config, this) }?.also {
                handled = true
            }
        }
        handleLocalFeatures(handled)
    }
    override suspend fun <R> withBot(bot: Bot, block: suspend BotContext<MessageHandler>.() -> R): R {
        return LocationMessageHandler(update, bot, handlerData, locationsWrapper).block()
    }
}

