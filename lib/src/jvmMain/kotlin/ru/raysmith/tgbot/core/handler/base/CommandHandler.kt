package ru.raysmith.tgbot.core.handler.base

import io.ktor.client.*
import ru.raysmith.tgbot.core.Bot
import ru.raysmith.tgbot.core.BotConfig
import ru.raysmith.tgbot.core.BotContext
import ru.raysmith.tgbot.core.BotContextDsl
import ru.raysmith.tgbot.core.handler.BaseEventHandler
import ru.raysmith.tgbot.core.handler.HandlerDsl
import ru.raysmith.tgbot.model.bot.BotCommand
import ru.raysmith.tgbot.model.network.message.Message

@HandlerDsl
open class CommandHandler(
    val command: BotCommand, val message: Message,
    final override val bot: Bot,
    val handler: suspend CommandHandler.() -> Unit = { }
) : BaseEventHandler(), BotContext<CommandHandler> {
    override val client: HttpClient = bot.client
    override val botConfig: BotConfig = bot.botConfig

    override var messageId: Int? = message.messageId
    override var inlineMessageId: String? = null

    override fun getChatId() = message.chat.id
    override fun getChatIdOrThrow() = message.chat.id

    override suspend fun handle() {
        handler()
        handled = true
        handleLocalFeatures(handled)
    }

    @BotContextDsl
    override suspend fun <R> withBot(bot: Bot, block: suspend BotContext<CommandHandler>.() -> R): R {
        return CommandHandler(command, message, bot, handler).block()
    }
}

inline fun CommandHandler.isCommand(value: String, equalHandler: CommandHandler.(argsString: String?) -> Unit) {
    if (command.body == value) {
        equalHandler(command.argsString)
    }
}