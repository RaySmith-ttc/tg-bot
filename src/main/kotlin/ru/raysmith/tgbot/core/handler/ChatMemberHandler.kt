package ru.raysmith.tgbot.core.handler

import ru.raysmith.tgbot.core.Bot
import ru.raysmith.tgbot.core.BotContext
import ru.raysmith.tgbot.core.EventHandler
import ru.raysmith.tgbot.core.HandlerDsl
import ru.raysmith.tgbot.model.network.User
import ru.raysmith.tgbot.model.network.chat.Chat
import ru.raysmith.tgbot.model.network.chat.ChatInviteLink
import ru.raysmith.tgbot.model.network.chat.ChatMember
import ru.raysmith.tgbot.model.network.chat.ChatMemberUpdated
import ru.raysmith.tgbot.network.TelegramApi
import ru.raysmith.tgbot.network.TelegramService

@HandlerDsl
class ChatMemberHandler(
    val chat: Chat,
    val from: User,
    val date: Int,
    val oldChatMember: ChatMember,
    val newChatMember: ChatMember,
    val inviteLink: ChatInviteLink? = null,
    private val handler: ChatMemberHandler.() -> Unit
) : EventHandler, BotContext<ChatMemberHandler> {

    override fun getChatId() = chat.id.toString()
    override var messageId: Int? = null
    override var inlineMessageId: String? = null

    constructor(chatMember: ChatMemberUpdated, handler: ChatMemberHandler.() -> Unit) : this(
        chatMember.chat, chatMember.from, chatMember.date, chatMember.oldChatMember, chatMember.newChatMember,
        chatMember.inviteLink, handler
    )

    override suspend fun handle() = handler()

    override var service: TelegramService = TelegramApi.service
    override fun withBot(bot: Bot, block: BotContext<ChatMemberHandler>.() -> Any) {
        ChatMemberHandler(chat, from, date, oldChatMember, newChatMember, inviteLink, handler).apply {
            this.service = bot.service
            block()
        }
    }
}