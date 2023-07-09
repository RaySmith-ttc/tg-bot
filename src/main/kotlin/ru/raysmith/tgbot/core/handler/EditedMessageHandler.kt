package ru.raysmith.tgbot.core.handler

import ru.raysmith.tgbot.core.Bot
import ru.raysmith.tgbot.core.BotContext
import ru.raysmith.tgbot.core.EventHandler
import ru.raysmith.tgbot.core.HandlerDsl
import ru.raysmith.tgbot.model.network.message.Message
import ru.raysmith.tgbot.network.TelegramFileService
import ru.raysmith.tgbot.network.TelegramService

@HandlerDsl
open class EditedMessageHandler(
    val message: Message,
    override val service: TelegramService, override val fileService: TelegramFileService,
    private val handler: EditedMessageHandler.() -> Unit = { }
) : EventHandler, BotContext<EditedMessageHandler> {

    val editDate = message.editDate!!
    override fun getChatId() = message.chat.id
    override fun getChatIdOrThrow() = message.chat.id
    override var messageId: Int? = message.messageId
    override var inlineMessageId: String? = null

    override suspend fun handle() = handler()

    override fun <R> withBot(bot: Bot, block: BotContext<EditedMessageHandler>.() -> R): R {
        return EditedMessageHandler(message, bot.service, bot.fileService, handler).block()
    }
}