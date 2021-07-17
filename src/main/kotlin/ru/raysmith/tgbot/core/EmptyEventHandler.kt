package ru.raysmith.tgbot.core

import retrofit2.Response
import ru.raysmith.tgbot.model.bot.TextMessage
import ru.raysmith.tgbot.model.network.message.response.MessageSendResponse

class EmptyEventHandler : EventHandler {
    override suspend fun handle() {
        //do nothing..
    }

    override var chatId: String? = null
    override var messageId: Long? = null
    override var inlineMessageId: String? = null

    override fun send(message: TextMessage.() -> Unit): Response<MessageSendResponse> {
        throw NotImplementedError("")
    }
}