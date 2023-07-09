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
import ru.raysmith.tgbot.network.TelegramFileService
import ru.raysmith.tgbot.network.TelegramService

@HandlerDsl
open class ChatMemberHandler(
    val chat: Chat,
    val from: User,
    val date: Int,
    val oldChatMember: ChatMember,
    val newChatMember: ChatMember,
    val inviteLink: ChatInviteLink? = null,
    override val service: TelegramService, override val fileService: TelegramFileService,
    private val handler: ChatMemberHandler.() -> Unit = { }
) : EventHandler, BotContext<ChatMemberHandler> {

    override fun getChatId() = chat.id
    override fun getChatIdOrThrow() = chat.id
    override var messageId: Int? = null
    override var inlineMessageId: String? = null

    constructor(chatMember: ChatMemberUpdated, service: TelegramService, fileService: TelegramFileService, handler: ChatMemberHandler.() -> Unit = { }) : this(
        chatMember.chat, chatMember.from, chatMember.date, chatMember.oldChatMember, chatMember.newChatMember,
        chatMember.inviteLink, service, fileService, handler
    )

    override suspend fun handle() = handler()

    override fun <R> withBot(bot: Bot, block: BotContext<ChatMemberHandler>.() -> R): R {
        return ChatMemberHandler(
            chat, from, date, oldChatMember, newChatMember, inviteLink, bot.service, bot.fileService, handler
        ).block()
    }
}

