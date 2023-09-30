package ru.raysmith.tgbot.core.handler.base

import io.ktor.client.*
import ru.raysmith.tgbot.core.Bot
import ru.raysmith.tgbot.core.BotContext
import ru.raysmith.tgbot.core.handler.EventHandler
import ru.raysmith.tgbot.core.handler.HandlerDsl
import ru.raysmith.tgbot.model.network.User
import ru.raysmith.tgbot.model.network.chat.Chat
import ru.raysmith.tgbot.model.network.chat.ChatInviteLink
import ru.raysmith.tgbot.model.network.chat.member.ChatMember
import ru.raysmith.tgbot.model.network.chat.member.ChatMemberUpdated

@HandlerDsl
open class ChatMemberHandler(
    @get:JvmName("chatOfHandler")
    val chat: Chat,
    val from: User,
    val date: Int,
    val oldChatMember: ChatMember,
    val newChatMember: ChatMember,
    val inviteLink: ChatInviteLink? = null,
    override val client: HttpClient,
    private val handler: suspend ChatMemberHandler.() -> Unit = { }
) : EventHandler, BotContext<ChatMemberHandler> {

    override fun getChatId() = chat.id
    override fun getChatIdOrThrow() = chat.id
    override var messageId: Int? = null
    override var inlineMessageId: String? = null

    constructor(chatMember: ChatMemberUpdated, client: HttpClient, handler: suspend ChatMemberHandler.() -> Unit = { }) : this(
        chatMember.chat, chatMember.from, chatMember.date, chatMember.oldChatMember, chatMember.newChatMember,
        chatMember.inviteLink, client, handler
    )

    override suspend fun handle() = handler()

    override suspend fun <R> withBot(bot: Bot, block: suspend BotContext<ChatMemberHandler>.() -> R): R {
        return ChatMemberHandler(
            chat, from, date, oldChatMember, newChatMember, inviteLink, bot.client, handler
        ).block()
    }
}

