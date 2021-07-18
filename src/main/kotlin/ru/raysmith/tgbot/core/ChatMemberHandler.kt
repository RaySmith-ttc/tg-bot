package ru.raysmith.tgbot.core

import ru.raysmith.tgbot.model.network.User
import ru.raysmith.tgbot.model.network.chat.Chat
import ru.raysmith.tgbot.model.network.chat.ChatInviteLink
import ru.raysmith.tgbot.model.network.chat.ChatMember
import ru.raysmith.tgbot.model.network.chat.ChatMemberUpdated

class ChatMemberHandler(
    val chat: Chat,
    val from: User,
    val date: Int,
    val oldChatMember: ChatMember,
    val newChatMember: ChatMember,
    val inviteLink: ChatInviteLink? = null,
    private val handler: ChatMemberHandler.() -> Unit
) : EventHandler {

    constructor(chatMember: ChatMemberUpdated, handler: ChatMemberHandler.() -> Unit) : this(
        chatMember.chat, chatMember.from, chatMember.date, chatMember.oldChatMember, chatMember.newChatMember,
        chatMember.inviteLink, handler
    )

    override suspend fun handle() = handler()
    override var chatId: String? = chat.id.toString()
    override var messageId: Long? = null
    override var inlineMessageId: String? = null
}