package ru.raysmith.tgbot.core

import ru.raysmith.tgbot.model.network.User
import ru.raysmith.tgbot.model.network.message.Message

class MessageHandler(val message: Message, val user: User, private val handler: MessageHandler.() -> Unit) : EventHandler, ISender, IEditor {
    val messageText = message.text

    override suspend fun handle() = handler()
    override var chatId: String? = user.id.toString()
    override var messageId: Long? = message.messageId
    override var inlineMessageId: String? = null
}