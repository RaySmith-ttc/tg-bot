package ru.raysmith.tgbot.core.handler

import ru.raysmith.tgbot.core.EventHandler
import ru.raysmith.tgbot.core.IEditor
import ru.raysmith.tgbot.core.ISender
import ru.raysmith.tgbot.model.network.User
import ru.raysmith.tgbot.model.network.message.Message

open class MessageHandler(val message: Message, val from: User, private val handler: MessageHandler.() -> Unit) : EventHandler,
    ISender, IEditor {
    val messageText = message.text

    override suspend fun handle() = handler()
    override var chatId: String? = from.id.toString()
    override var messageId: Long? = message.messageId
    override var inlineMessageId: String? = null
}