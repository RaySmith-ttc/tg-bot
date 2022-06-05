package ru.raysmith.tgbot.core.handler

import ru.raysmith.tgbot.core.*
import ru.raysmith.tgbot.model.bot.BotCommand
import ru.raysmith.tgbot.model.network.message.Message
import ru.raysmith.tgbot.network.TelegramApi
import ru.raysmith.tgbot.network.TelegramService

@HandlerDsl
class CommandHandler(
    val command: BotCommand,
    val message: Message,
    val handler: CommandHandler.() -> Unit
) : EventHandler, BotContext<CommandHandler> {

    override fun getChatId() = message.chat.id.toString()
    override var messageId: Int? = null
    override var inlineMessageId: String? = null

    override var service: TelegramService = TelegramApi.service
    override suspend fun handle() = handler()

    override fun withBot(bot: Bot, block: BotContext<CommandHandler>.() -> Any) {
        CommandHandler(command, message, handler).apply {
            this.service = bot.service
            this.block()
        }
    }
}

inline fun CommandHandler.isCommand(value: String, equalHandler: CommandHandler.(argsString: String?) -> Unit) {
    if (command.body == value) {
        equalHandler(command.argsString)
    }
}