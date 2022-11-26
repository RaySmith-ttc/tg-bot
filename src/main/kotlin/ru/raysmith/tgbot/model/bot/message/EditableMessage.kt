package ru.raysmith.tgbot.model.bot.message

import ru.raysmith.tgbot.core.ApiCaller
import ru.raysmith.tgbot.model.bot.ChatId
import ru.raysmith.tgbot.model.bot.message.keyboard.KeyboardCreator
import ru.raysmith.tgbot.model.bot.message.keyboard.MessageKeyboard
import ru.raysmith.tgbot.model.network.chat.Chat
import ru.raysmith.tgbot.model.network.response.MessageResponse
import ru.raysmith.tgbot.network.TelegramFileService
import ru.raysmith.tgbot.network.TelegramService
import ru.raysmith.tgbot.utils.errorBody
import ru.raysmith.tgbot.utils.noimpl

interface MessageWithReplyMarkup : EditableMessage, KeyboardCreator {
    companion object {
        internal fun instance(apiCaller: ApiCaller) = object : MessageWithReplyMarkup {
            override val service: TelegramService = apiCaller.service
            override val fileService: TelegramFileService = apiCaller.fileService
            override var disableNotification: Boolean? = null
            override var replyToMessageId: Int? = null
            override var allowSendingWithoutReply: Boolean? = null
            override var protectContent: Boolean? = null
            override var keyboardMarkup: MessageKeyboard? = null
            override fun edit(chatId: ChatId?, messageId: Int?, inlineMessageId: String?) = noimpl()
            override fun send(chatId: ChatId) = noimpl()
        }
    }

    override fun editReplyMarkup(chatId: ChatId?, messageId: Int?, inlineMessageId: String?): MessageResponse {
        return service.editMessageReplyMarkup(
            chatId = chatId,
            messageId = messageId,
            inlineMessageId = inlineMessageId,
            replyMarkup = keyboardMarkup?.toMarkup()
        ).execute().body() ?: errorBody()
    }
}

interface EditableMessage : IMessage<MessageResponse> {

    /** Edit message from [chat] with [messageId] or [inlineMessageId] */
    fun edit(chat: Chat?, messageId: Int?, inlineMessageId: String?): MessageResponse =
        edit(chat?.id, messageId, inlineMessageId)

    /** Edit message from chat with [chatId] and [messageId] or [inlineMessageId] */
    fun edit(chatId: ChatId?, messageId: Int?, inlineMessageId: String?): MessageResponse // TODO return Message?

    fun editReplyMarkup(chatId: ChatId?, messageId: Int?, inlineMessageId: String?): MessageResponse
}