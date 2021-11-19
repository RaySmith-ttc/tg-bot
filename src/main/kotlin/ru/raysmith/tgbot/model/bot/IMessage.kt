package ru.raysmith.tgbot.model.bot

import retrofit2.Response
import ru.raysmith.tgbot.model.network.User
import ru.raysmith.tgbot.model.network.chat.Chat
import ru.raysmith.tgbot.model.network.message.MessageResponse

interface IMessage {
    var disableNotification: Boolean?
    var replyToMessageId: Int?
    var allowSendingWithoutReply: Boolean?

    fun send(user: User): Response<MessageResponse> = send(user.id.toString())
    fun send(chat: Chat): Response<MessageResponse> = send(chat.id.toString())
    fun send(chatId: String): Response<MessageResponse>
}

interface EditableMessage : IMessage {
    fun edit(chatId: String?, messageId: Long?, inlineMessageId: String?): Response<MessageResponse>
}