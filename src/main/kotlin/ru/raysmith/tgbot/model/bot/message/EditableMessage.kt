package ru.raysmith.tgbot.model.bot.message

import ru.raysmith.tgbot.model.bot.ChatId
import ru.raysmith.tgbot.model.network.chat.Chat
import ru.raysmith.tgbot.model.network.response.MessageResponse

interface EditableMessage : IMessage<MessageResponse> {

    /** Edit message from [chat] with [messageId] or [inlineMessageId] */
    fun edit(chat: Chat?, messageId: Int?, inlineMessageId: String?): MessageResponse =
        edit(chat?.id, messageId, inlineMessageId)

    /** Edit message from chat with [chatId] and [messageId] or [inlineMessageId] */
    fun edit(chatId: ChatId?, messageId: Int?, inlineMessageId: String?): MessageResponse
}