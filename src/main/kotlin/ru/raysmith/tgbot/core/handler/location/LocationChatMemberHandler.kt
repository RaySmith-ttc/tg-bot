package ru.raysmith.tgbot.core.handler.location

import ru.raysmith.tgbot.core.*
import ru.raysmith.tgbot.core.handler.ChatMemberHandler
import ru.raysmith.tgbot.model.network.updates.Update
import ru.raysmith.tgbot.network.TelegramFileService
import ru.raysmith.tgbot.network.TelegramService
import ru.raysmith.tgbot.utils.locations.LocationConfig
import ru.raysmith.tgbot.utils.locations.LocationsWrapper

data class LocationChatMemberHandlerData<T : LocationConfig>(
    val handler: (context(T) LocationChatMemberHandler<T>.() -> Unit)? = null
)

@HandlerDsl
class LocationChatMemberHandler<T : LocationConfig>(
    override val update: Update, service: TelegramService, fileService: TelegramFileService,
    private val handlerData: MutableMap<String, LocationChatMemberHandlerData<T>>,
    override val locationsWrapper: LocationsWrapper<T>
) : ChatMemberHandler(update.myChatMember!!, service, fileService), LocationHandler<T> {
    
    override val config by lazy { config() }
    override fun getChatId() = chat.id
    override fun getChatIdOrThrow() = chat.id
    override var messageId: Int? = null
    override var inlineMessageId: String? = null
    
    override suspend fun handle() {
        handlerData.forEach {
            it.value.handler?.let { it1 -> it1(config, this) }
        }
    }
    
    override fun withBot(bot: Bot, block: BotContext<ChatMemberHandler>.() -> Any) {
        LocationChatMemberHandler<T>(update, service, fileService, handlerData, locationsWrapper).apply {
            block()
        }
    }
}