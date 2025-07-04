package ru.raysmith.tgbot.core.handler.base

import io.ktor.client.*
import ru.raysmith.tgbot.core.Bot
import ru.raysmith.tgbot.core.BotConfig
import ru.raysmith.tgbot.core.BotContext
import ru.raysmith.tgbot.core.handler.BaseEventHandler
import ru.raysmith.tgbot.model.network.User
import ru.raysmith.tgbot.model.network.chat.Chat
import ru.raysmith.tgbot.model.network.chat.ChatInviteLink
import ru.raysmith.tgbot.model.network.chat.member.ChatMember
import ru.raysmith.tgbot.model.network.chat.member.ChatMemberUpdated

open class ChatMemberHandler(
    @get:JvmName("chatOfHandler") // TODO ?
    val chat: Chat,
    val from: User,
    val date: Int,
    val oldChatMember: ChatMember,
    val newChatMember: ChatMember,
    val inviteLink: ChatInviteLink? = null,
    final override val bot: Bot,
    private val handler: suspend ChatMemberHandler.() -> Unit = { }
) : BaseEventHandler(), BotContext<ChatMemberHandler> {
    override val client: HttpClient = bot.client
    override val botConfig: BotConfig = bot.botConfig

    override var messageId: Int? = null
    override var inlineMessageId: String? = null

    constructor(chatMember: ChatMemberUpdated, bot: Bot, handler: suspend ChatMemberHandler.() -> Unit = { }) : this(
        chatMember.chat, chatMember.from, chatMember.date, chatMember.oldChatMember, chatMember.newChatMember,
        chatMember.inviteLink, bot, handler
    )

    override fun getChatId() = chat.id
    override fun getChatIdOrThrow() = chat.id

    override suspend fun handle() {
        handler()
        handled = true
        handleLocalFeatures(handled)
    }

    override suspend fun <R> withBot(bot: Bot, block: suspend BotContext<ChatMemberHandler>.() -> R): R {
        return ChatMemberHandler(
            chat, from, date, oldChatMember, newChatMember, inviteLink, bot, handler
        ).block()
    }
}

