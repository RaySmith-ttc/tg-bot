package ru.raysmith.tgbot.core.handler.base

import io.ktor.client.*
import ru.raysmith.tgbot.core.Bot
import ru.raysmith.tgbot.core.BotContext
import ru.raysmith.tgbot.core.handler.EventHandler
import ru.raysmith.tgbot.core.handler.HandlerDsl
import ru.raysmith.tgbot.model.network.message.Message

@HandlerDsl
open class EditedMessageHandler(
    val message: Message,
    override val client: HttpClient,
    private val handler: EditedMessageHandler.() -> Unit = { }
) : EventHandler, BotContext<EditedMessageHandler> {

    val editDate = message.editDate!!
    override fun getChatId() = message.chat.id
    override fun getChatIdOrThrow() = message.chat.id
    override var messageId: Int? = message.messageId
    override var inlineMessageId: String? = null

    override fun handle() = handler()

    override fun <R> withBot(bot: Bot, block: BotContext<EditedMessageHandler>.() -> R): R {
        return EditedMessageHandler(message, bot.client, handler).block()
    }
}