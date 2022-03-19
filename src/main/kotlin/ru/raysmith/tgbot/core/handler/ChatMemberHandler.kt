package ru.raysmith.tgbot.core.handler

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
) : EventHandler {

    override fun getChatId() = chat.id.toString()
    override var messageId: Long? = null
    override var inlineMessageId: String? = null

    constructor(chatMember: ChatMemberUpdated, handler: ChatMemberHandler.() -> Unit) : this(
        chatMember.chat, chatMember.from, chatMember.date, chatMember.oldChatMember, chatMember.newChatMember,
        chatMember.inviteLink, handler
    )

    override suspend fun handle() = handler()

    override var service: TelegramService = TelegramApi.service
    fun withService(service: TelegramService, block: () -> Any) {
        ChatMemberHandler(chat, from, date, oldChatMember, newChatMember, inviteLink, handler).apply {
            this.service = service
            block()
        }
    }
}