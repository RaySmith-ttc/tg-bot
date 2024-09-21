package ru.raysmith.tgbot.model.bot.message

import io.ktor.client.*
import ru.raysmith.tgbot.core.Bot
import ru.raysmith.tgbot.core.BotConfig
import ru.raysmith.tgbot.model.bot.ChatId
import ru.raysmith.tgbot.model.bot.message.keyboard.KeyboardCreator
import ru.raysmith.tgbot.model.bot.message.keyboard.MessageKeyboard
import ru.raysmith.tgbot.model.network.chat.Chat
import ru.raysmith.tgbot.model.network.message.Message
import ru.raysmith.tgbot.model.network.message.ReplyParameters
import ru.raysmith.tgbot.utils.noimpl

interface MessageWithReplyMarkup : EditableMessage, KeyboardCreator, ExtendedMessage<Message> {
    companion object {
        internal fun instance(bot: Bot) = object : MessageWithReplyMarkup {
            override val client: HttpClient = bot.client
            override val botConfig: BotConfig = bot.botConfig
            override var messageThreadId: Int? = null
            override var disableNotification: Boolean? = null
            override var replyParameters: ReplyParameters? = null
            override var protectContent: Boolean? = null
            override var messageEffectId: String? = null
            override var businessConnectionId: String? = null
            override var keyboardMarkup: MessageKeyboard? = null
            override suspend fun edit(chatId: ChatId?, messageId: Int?, inlineMessageId: String?) = noimpl()
            override suspend fun send(chatId: ChatId) = noimpl()
        }
    }

    override suspend fun editReplyMarkup(chatId: ChatId?, messageId: Int?, inlineMessageId: String?): Message {
        return editMessageReplyMarkup(
            businessConnectionId = businessConnectionId,
            chatId = chatId,
            messageId = messageId,
            inlineMessageId = inlineMessageId,
            replyMarkup = keyboardMarkup?.toMarkup()
        )
    }
}

interface EditableMessage : IMessage<Message> {

    /** Edit message from [chat] with [messageId] or [inlineMessageId] */
    suspend fun edit(chat: Chat?, messageId: Int?, inlineMessageId: String?): Message =
        edit(chat?.id, messageId, inlineMessageId)

    /** Edit message from chat with [chatId] and [messageId] or [inlineMessageId] */
    suspend fun edit(chatId: ChatId?, messageId: Int?, inlineMessageId: String?): Message

    suspend fun editReplyMarkup(chatId: ChatId?, messageId: Int?, inlineMessageId: String?): Message
}