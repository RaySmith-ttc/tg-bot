package ru.raysmith.tgbot.core.handler.base

import ru.raysmith.tgbot.core.Bot
import ru.raysmith.tgbot.core.BotContext
import ru.raysmith.tgbot.core.handler.BaseEventHandler
import ru.raysmith.tgbot.core.handler.HandlerDsl
import ru.raysmith.tgbot.model.network.Poll

@HandlerDsl
open class PollHandler(
    val poll: Poll,
    final override val bot: Bot,
    private val handler: suspend PollHandler.() -> Unit = {}
) : BaseEventHandler(), BotContext<PollHandler> {
    override fun getChatId() = null
    override var messageId: Int? = null
    override var inlineMessageId: String? = null

    override suspend fun handle() {
        handler()
        handled = true
        handleLocalFeatures(handled)
    }

    override suspend fun <R> withBot(bot: Bot, block: suspend BotContext<PollHandler>.() -> R): R {
        return PollHandler(poll, bot, handler).block()
    }
}

