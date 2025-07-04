package ru.raysmith.tgbot.core.handler.base

import io.ktor.client.*
import ru.raysmith.tgbot.core.Bot
import ru.raysmith.tgbot.core.BotConfig
import ru.raysmith.tgbot.core.BotContext
import ru.raysmith.tgbot.core.handler.BaseEventHandler
import ru.raysmith.tgbot.model.network.message.Message

open class EditedMessageHandler(
    val message: Message,
    final override val bot: Bot,
    private val handler: suspend EditedMessageHandler.() -> Unit = { }
) : BaseEventHandler(), BotContext<EditedMessageHandler> {
    override val client: HttpClient = bot.client
    override val botConfig: BotConfig = bot.botConfig

    val editDate = message.editDate!!
    override var messageId: Int? = message.messageId
    override var inlineMessageId: String? = null

    override fun getChatId() = message.chat.id
    override fun getChatIdOrThrow() = message.chat.id

    override suspend fun handle() {
        handler()
        handled = true
        handleLocalFeatures(handled)
    }

    override suspend fun <R> withBot(bot: Bot, block: suspend BotContext<EditedMessageHandler>.() -> R): R {
        return EditedMessageHandler(message, bot, handler).block()
    }
}