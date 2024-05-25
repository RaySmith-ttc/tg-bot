package ru.raysmith.tgbot.core.handler.location

import ru.raysmith.tgbot.core.Bot
import ru.raysmith.tgbot.core.BotContext
import ru.raysmith.tgbot.core.handler.HandlerDsl
import ru.raysmith.tgbot.core.handler.LocationHandler
import ru.raysmith.tgbot.core.handler.base.ChatJoinRequestHandler
import ru.raysmith.tgbot.model.network.updates.Update
import ru.raysmith.tgbot.utils.locations.LocationConfig
import ru.raysmith.tgbot.utils.locations.LocationsWrapper

data class LocationChatJoinRequestHandlerData<T : LocationConfig>(
    val handler: (suspend context(T) LocationChatJoinRequestHandler<T>.() -> Unit)? = null
)

@HandlerDsl
class LocationChatJoinRequestHandler<T : LocationConfig>(
    override val update: Update, bot: Bot,
    private val handlerData: MutableList<LocationChatJoinRequestHandlerData<T>>,
    override val locationsWrapper: LocationsWrapper<T>
) : ChatJoinRequestHandler(update.chatJoinRequest!!, bot), LocationHandler<T> {

    override val config by lazy { config() }
    override suspend fun handle() {
        handlerData.forEach {
            it.handler?.let { it1 -> it1(config, this) }?.also {
                handled = true
            }
        }
        handleLocalFeatures(handled)
    }
    override suspend fun <R> withBot(bot: Bot, block: suspend BotContext<ChatJoinRequestHandler>.() -> R): R {
        return LocationChatJoinRequestHandler(update, bot, handlerData, locationsWrapper).block()
    }
}