package ru.raysmith.tgbot.model.bot

import retrofit2.Response
import ru.raysmith.tgbot.core.ApiCaller
import ru.raysmith.tgbot.model.network.chat.Chat
import ru.raysmith.tgbot.model.network.message.MessageRes
import ru.raysmith.tgbot.model.network.message.MessageResponse
import ru.raysmith.tgbot.network.TelegramService

interface IMessage<T : MessageRes> : ApiCaller {
    companion object {
        const val MAX_TEXT_LENGTH = 4096
    }

    var disableNotification: Boolean?
    var replyToMessageId: Int?
    var allowSendingWithoutReply: Boolean?

    fun send(chat: Chat): Response<T> = send(chat.id.toString())
    fun send(chatId: String): Response<T>
}

interface EditableMessage : IMessage<MessageResponse> {
    fun edit(chatId: String?, messageId: Long?, inlineMessageId: String?): Response<MessageResponse>
}