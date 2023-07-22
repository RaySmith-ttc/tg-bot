package ru.raysmith.tgbot.core.handler.base

import ru.raysmith.tgbot.core.Bot
import ru.raysmith.tgbot.core.BotContext
import ru.raysmith.tgbot.core.handler.EventHandler
import ru.raysmith.tgbot.core.handler.HandlerDsl
import ru.raysmith.tgbot.model.network.Poll
import ru.raysmith.tgbot.network.TelegramFileService
import ru.raysmith.tgbot.network.TelegramService

@HandlerDsl
open class PollHandler(
    val poll: Poll,
    override val service: TelegramService, override val fileService: TelegramFileService,
    private val handler: PollHandler.() -> Unit = {}
) : EventHandler, BotContext<PollHandler> {

    override fun getChatId() = null
    override var messageId: Int? = null
    override var inlineMessageId: String? = null

    override suspend fun handle() = handler()

    override fun <R> withBot(bot: Bot, block: BotContext<PollHandler>.() -> R): R {
        return PollHandler(poll, bot.service, bot.fileService, handler).block()
    }
}

