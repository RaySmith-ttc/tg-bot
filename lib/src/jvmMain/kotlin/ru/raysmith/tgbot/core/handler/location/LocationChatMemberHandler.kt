package ru.raysmith.tgbot.core.handler.location

import ru.raysmith.tgbot.core.Bot
import ru.raysmith.tgbot.core.BotContext
import ru.raysmith.tgbot.core.handler.LocationHandler
import ru.raysmith.tgbot.core.handler.base.ChatMemberHandler
import ru.raysmith.tgbot.model.network.updates.Update
import ru.raysmith.tgbot.utils.locations.LocationFlowContext
import ru.raysmith.tgbot.utils.locations.LocationsWrapper

data class LocationChatMemberHandlerData<LFC : LocationFlowContext>(
    val handler: (suspend context(LFC) LocationChatMemberHandler<LFC>.() -> Unit)? = null
)

class LocationChatMemberHandler<LFC : LocationFlowContext>(
    override val update: Update, bot: Bot,
    private val handlerData: MutableList<LocationChatMemberHandlerData<LFC>>,
    override val locationsWrapper: LocationsWrapper<LFC>
) : ChatMemberHandler(update.myChatMember!!, bot), LocationHandler<LFC, ChatMemberHandler> {
    
    override val locationFlowContext by lazy { locationFlowContext() }
    override fun getChatId() = chat.id
    override fun getChatIdOrThrow() = chat.id
    override var messageId: Int? = null
    override var inlineMessageId: String? = null
    
    override suspend fun handle() {
        handlerData.forEach {
            it.handler?.let { it1 -> it1(locationFlowContext, this) }?.also {
                handled = true
            }
        }
        handleLocalFeatures(handled)
    }

    override suspend fun <R> withBot(bot: Bot, block: suspend BotContext<ChatMemberHandler>.() -> R): R {
        return LocationChatMemberHandler(update, bot, handlerData, locationsWrapper).block()
    }
}