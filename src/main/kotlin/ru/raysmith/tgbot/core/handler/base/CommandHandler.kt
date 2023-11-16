package ru.raysmith.tgbot.core.handler.base

import io.ktor.client.*
import ru.raysmith.tgbot.core.Bot
import ru.raysmith.tgbot.core.BotContext
import ru.raysmith.tgbot.core.handler.EventHandler
import ru.raysmith.tgbot.core.handler.HandlerDsl
import ru.raysmith.tgbot.model.bot.BotCommand
import ru.raysmith.tgbot.model.network.message.Message
import ru.raysmith.tgbot.utils.BotContextDsl

@HandlerDsl
open class CommandHandler(
    val command: BotCommand, val message: Message,
    override val client: HttpClient,
    val handler: suspend CommandHandler.() -> Unit = { }
) : EventHandler, BotContext<CommandHandler> {

    override fun getChatId() = message.chat.id
    override fun getChatIdOrThrow() = message.chat.id
    override var messageId: Int? = message.messageId
    override var inlineMessageId: String? = null

    override suspend fun handle() = handler()

    @BotContextDsl
    override suspend fun <R> withBot(bot: Bot, block: suspend BotContext<CommandHandler>.() -> R): R {
        return CommandHandler(command, message, bot.client, handler).block()
    }
}

suspend inline fun CommandHandler.isCommand(value: String, crossinline equalHandler: suspend CommandHandler.(argsString: String?) -> Unit) {
    if (command.body == value) {
        equalHandler(command.argsString)
    }
}