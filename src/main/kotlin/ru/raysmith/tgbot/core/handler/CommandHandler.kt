package ru.raysmith.tgbot.core.handler

import ru.raysmith.tgbot.core.Bot
import ru.raysmith.tgbot.core.BotContext
import ru.raysmith.tgbot.core.EventHandler
import ru.raysmith.tgbot.core.HandlerDsl
import ru.raysmith.tgbot.model.bot.BotCommand
import ru.raysmith.tgbot.model.network.message.Message
import ru.raysmith.tgbot.network.TelegramFileService
import ru.raysmith.tgbot.network.TelegramService
import ru.raysmith.tgbot.utils.BotContextDsl

@HandlerDsl
class CommandHandler(
    val command: BotCommand,
    val message: Message,
    override val service: TelegramService, override val fileService: TelegramFileService,
    val handler: CommandHandler.() -> Unit
) : EventHandler, BotContext<CommandHandler> {

    override fun getChatId() = message.chat.id
    override var messageId: Int? = null
    override var inlineMessageId: String? = null

    override suspend fun handle() = handler()

    @BotContextDsl
    override fun withBot(bot: Bot, block: BotContext<CommandHandler>.() -> Any) {
        CommandHandler(command, message, bot.service, bot.fileService, handler).apply {
            this.block()
        }
    }
}

inline fun CommandHandler.isCommand(value: String, equalHandler: CommandHandler.(argsString: String?) -> Unit) {
    if (command.body == value) {
        equalHandler(command.argsString)
    }
}