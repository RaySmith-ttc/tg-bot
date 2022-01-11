package ru.raysmith.tgbot.model.bot

import retrofit2.Response
import ru.raysmith.tgbot.model.network.User
import ru.raysmith.tgbot.model.network.chat.Chat
import ru.raysmith.tgbot.model.network.message.MessageRes
import ru.raysmith.tgbot.model.network.message.MessageResponse

interface IMessage<T : MessageRes> {
    var disableNotification: Boolean?
    var replyToMessageId: Int?
    var allowSendingWithoutReply: Boolean?

    fun send(user: User): Response<T> = send(user.id.toString())
    fun send(chat: Chat): Response<T> = send(chat.id.toString())
    fun send(chatId: String): Response<T>
}

interface EditableMessage : IMessage<MessageResponse> {
    fun edit(chatId: String?, messageId: Long?, inlineMessageId: String?): Response<MessageResponse>
}