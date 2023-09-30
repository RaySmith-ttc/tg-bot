package ru.raysmith.tgbot.core.handler.base

import io.ktor.client.*
import ru.raysmith.tgbot.core.Bot
import ru.raysmith.tgbot.core.BotContext
import ru.raysmith.tgbot.core.handler.EventHandler
import ru.raysmith.tgbot.core.handler.HandlerDsl
import ru.raysmith.tgbot.model.network.chat.ChatJoinRequest

@HandlerDsl
open class ChatJoinRequestHandler(
    val chatJoinRequest: ChatJoinRequest,
    override val client: HttpClient,
    private val handler: suspend ChatJoinRequestHandler.() -> Unit = { }
) : EventHandler, BotContext<ChatJoinRequestHandler> {

    override fun getChatId() = chatJoinRequest.chat.id
    override var messageId: Int? = null
    override var inlineMessageId: String? = null

    override suspend fun handle() = handler()


    suspend fun approve() = approveChatJoinRequest(chatJoinRequest.userChatId)
    suspend fun decline() = declineChatJoinRequest(chatJoinRequest.userChatId)

    override suspend fun <R> withBot(bot: Bot, block: suspend BotContext<ChatJoinRequestHandler>.() -> R): R {
        return ChatJoinRequestHandler(chatJoinRequest, client, handler).block()
    }
}