package ru.raysmith.tgbot.core.handler.location

import ru.raysmith.tgbot.core.Bot
import ru.raysmith.tgbot.core.BotContext
import ru.raysmith.tgbot.core.handler.LocationHandler
import ru.raysmith.tgbot.core.handler.base.ChatMemberHandler
import ru.raysmith.tgbot.model.network.updates.Update
import ru.raysmith.tgbot.utils.locations.LocationFlowContext
import ru.raysmith.tgbot.utils.locations.LocationsWrapper

data class LocationChatMemberHandlerData<T : LocationFlowContext>(
    val handler: (suspend context(T) LocationChatMemberHandler<T>.() -> Unit)? = null
)

class LocationChatMemberHandler<T : LocationFlowContext>(
    override val update: Update, bot: Bot,
    private val handlerData: MutableList<LocationChatMemberHandlerData<T>>,
    override val locationsWrapper: LocationsWrapper<T>
) : ChatMemberHandler(update.myChatMember!!, bot), LocationHandler<T, ChatMemberHandler> {
    
    override val config by lazy { config() }
    override fun getChatId() = chat.id
    override fun getChatIdOrThrow() = chat.id
    override var messageId: Int? = null
    override var inlineMessageId: String? = null
    
    override suspend fun handle() {
        handlerData.forEach {
            it.handler?.let { it1 -> it1(config, this) }?.also {
                handled = true
            }
        }
        handleLocalFeatures(handled)
    }

    override suspend fun <R> withBot(bot: Bot, block: suspend BotContext<ChatMemberHandler>.() -> R): R {
        return LocationChatMemberHandler(update, bot, handlerData, locationsWrapper).block()
    }
}