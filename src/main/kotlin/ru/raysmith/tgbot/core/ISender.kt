package ru.raysmith.tgbot.core

import ru.raysmith.tgbot.core.handler.CallbackQueryHandler
import ru.raysmith.tgbot.model.bot.MediaGroupMessage
import ru.raysmith.tgbot.model.bot.PhotoMessage
import ru.raysmith.tgbot.model.bot.TextMessage
import ru.raysmith.tgbot.model.network.message.Message

interface ChatID {
    var chatId: String?
}

interface ISender : ChatID {

    private fun answeredIfCallbackQueryHandler() {
        if (this is CallbackQueryHandler) {
            isAnswered = true
        }
    }

    fun send(chatId: String = this.chatId!!, message: TextMessage.() -> Unit): Message {
        return (TextMessage().apply(message).send(chatId)
            .body()?.result ?: throw NullPointerException("Send message has no response body"))
            .also { answeredIfCallbackQueryHandler() }
    }

    fun sendPhoto(chatId: String = this.chatId!!, message: PhotoMessage.() -> Unit): Message {
        return (PhotoMessage().apply(message).send(chatId)
            .body()?.result ?: throw NullPointerException("Send photo has no response body"))
            .also { answeredIfCallbackQueryHandler() }
    }

    fun sendMediaGroup(chatId: String = this.chatId!!, message: MediaGroupMessage.() -> Unit): List<Message> {
        return (MediaGroupMessage().apply(message).send(chatId)
            .body()?.results ?: throw NullPointerException("Send media group has no response body"))
            .also { answeredIfCallbackQueryHandler() }
    }
}