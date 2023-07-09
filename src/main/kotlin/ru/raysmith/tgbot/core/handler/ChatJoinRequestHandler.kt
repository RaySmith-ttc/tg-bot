package ru.raysmith.tgbot.core.handler

import ru.raysmith.tgbot.core.Bot
import ru.raysmith.tgbot.core.BotContext
import ru.raysmith.tgbot.core.EventHandler
import ru.raysmith.tgbot.core.HandlerDsl
import ru.raysmith.tgbot.model.network.chat.*
import ru.raysmith.tgbot.network.TelegramFileService
import ru.raysmith.tgbot.network.TelegramService

@HandlerDsl
open class ChatJoinRequestHandler(
    val chatJoinRequest: ChatJoinRequest,
    override val service: TelegramService, override val fileService: TelegramFileService,
    private val handler: ChatJoinRequestHandler.() -> Unit = { }
) : EventHandler, BotContext<ChatJoinRequestHandler> {

    override fun getChatId() = chatJoinRequest.chat.id
    override var messageId: Int? = null
    override var inlineMessageId: String? = null

    override suspend fun handle() = handler()


    fun approve() = approveChatJoinRequest(chatJoinRequest.userChatId)
    fun decline() = declineChatJoinRequest(chatJoinRequest.userChatId)

    override fun <R> withBot(bot: Bot, block: BotContext<ChatJoinRequestHandler>.() -> R): R {
        return ChatJoinRequestHandler(chatJoinRequest, bot.service, bot.fileService, handler).block()
    }
}