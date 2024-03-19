package ru.raysmith.tgbot.core.handler.base

import ru.raysmith.tgbot.core.Bot
import ru.raysmith.tgbot.core.BotContext
import ru.raysmith.tgbot.core.handler.BaseEventHandler
import ru.raysmith.tgbot.core.handler.HandlerDsl
import ru.raysmith.tgbot.model.network.updates.Update

@HandlerDsl
class UnknownEventHandler(
    val update: Update,
    override val bot: Bot,
    val handler: suspend UnknownEventHandler.() -> Unit = {}
) : BaseEventHandler(), BotContext<UnknownEventHandler> {
    override suspend fun handle() {
        handler()
        handled = true
        handleLocalFeatures(handled)
    }
    override fun getChatId() = update.findChatId()

    override var messageId = update.message?.messageId
    override var inlineMessageId = update.callbackQuery?.inlineMessageId

    override suspend fun <R> withBot(bot: Bot, block: suspend BotContext<UnknownEventHandler>.() -> R): R {
        return UnknownEventHandler(update, bot, handler).block()
    }
}