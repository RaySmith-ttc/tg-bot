package ru.raysmith.tgbot.core.handler.base

import io.ktor.client.*
import ru.raysmith.tgbot.core.Bot
import ru.raysmith.tgbot.core.BotConfig
import ru.raysmith.tgbot.core.BotContext
import ru.raysmith.tgbot.core.handler.BaseEventHandler
import ru.raysmith.tgbot.model.network.chat.ChatJoinRequest

open class ChatJoinRequestHandler(
    val chatJoinRequest: ChatJoinRequest,
    final override val bot: Bot,
    private val handler: suspend ChatJoinRequestHandler.() -> Unit = { }
) : BaseEventHandler(), BotContext<ChatJoinRequestHandler> {
    override val client: HttpClient = bot.client
    override val botConfig: BotConfig = bot.botConfig

    override var messageId: Int? = null
    override var inlineMessageId: String? = null

    override fun getChatId() = chatJoinRequest.chat.id
    override fun getChatIdOrThrow() = chatJoinRequest.chat.id

    override suspend fun handle() {
        handler()
        handled = true
        handleLocalFeatures(handled)
    }

    suspend fun approve() = approveChatJoinRequest(chatJoinRequest.userChatId)
    suspend fun decline() = declineChatJoinRequest(chatJoinRequest.userChatId)

    override suspend fun <R> withBot(bot: Bot, block: suspend BotContext<ChatJoinRequestHandler>.() -> R): R {
        return ChatJoinRequestHandler(chatJoinRequest, bot, handler).block()
    }
}