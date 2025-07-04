package ru.raysmith.tgbot.core.handler.base

import io.ktor.client.*
import ru.raysmith.tgbot.core.Bot
import ru.raysmith.tgbot.core.BotConfig
import ru.raysmith.tgbot.core.BotContext
import ru.raysmith.tgbot.core.handler.BaseEventHandler
import ru.raysmith.tgbot.model.network.updates.Update

class UnknownEventHandler(
    val update: Update,
    override val bot: Bot,
    val handler: suspend UnknownEventHandler.() -> Unit = {}
) : BaseEventHandler(), BotContext<UnknownEventHandler> {
    override val client: HttpClient = bot.client
    override val botConfig: BotConfig = bot.botConfig

    override var messageId = update.message?.messageId
    override var inlineMessageId = update.callbackQuery?.inlineMessageId

    override fun getChatId() = update.findChatId()

    override suspend fun handle() {
        handler()
        handled = true
        handleLocalFeatures(handled)
    }

    override suspend fun <R> withBot(bot: Bot, block: suspend BotContext<UnknownEventHandler>.() -> R): R {
        return UnknownEventHandler(update, bot, handler).block()
    }
}