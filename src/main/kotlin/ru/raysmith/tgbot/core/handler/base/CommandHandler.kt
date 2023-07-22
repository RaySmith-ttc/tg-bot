package ru.raysmith.tgbot.core.handler.base

import ru.raysmith.tgbot.core.Bot
import ru.raysmith.tgbot.core.BotContext
import ru.raysmith.tgbot.core.handler.EventHandler
import ru.raysmith.tgbot.core.handler.HandlerDsl
import ru.raysmith.tgbot.model.bot.BotCommand
import ru.raysmith.tgbot.model.network.message.Message
import ru.raysmith.tgbot.network.TelegramFileService
import ru.raysmith.tgbot.network.TelegramService
import ru.raysmith.tgbot.utils.BotContextDsl

@HandlerDsl
open class CommandHandler(
    val command: BotCommand, val message: Message,
    override val service: TelegramService, override val fileService: TelegramFileService,
    val handler: CommandHandler.() -> Unit = { }
) : EventHandler, BotContext<CommandHandler> {

    override fun getChatId() = message.chat.id
    override fun getChatIdOrThrow() = message.chat.id
    override var messageId: Int? = null
    override var inlineMessageId: String? = null

    override suspend fun handle() = handler()

    @BotContextDsl
    override fun <R> withBot(bot: Bot, block: BotContext<CommandHandler>.() -> R): R {
        return CommandHandler(command, message, bot.service, bot.fileService, handler).block()
    }
}

inline fun CommandHandler.isCommand(value: String, equalHandler: CommandHandler.(argsString: String?) -> Unit) {
    if (command.body == value) {
        equalHandler(command.argsString)
    }
}