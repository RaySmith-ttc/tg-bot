package ru.raysmith.tgbot.core.handler.base

import ru.raysmith.tgbot.core.Bot
import ru.raysmith.tgbot.core.BotContext
import ru.raysmith.tgbot.core.handler.EventHandler
import ru.raysmith.tgbot.core.handler.HandlerDsl
import ru.raysmith.tgbot.model.network.ChosenInlineResult
import ru.raysmith.tgbot.network.TelegramFileService
import ru.raysmith.tgbot.network.TelegramService

@HandlerDsl
open class ChosenInlineQueryHandler(
    val inlineResult: ChosenInlineResult,
    override val service: TelegramService, override val fileService: TelegramFileService,
    private val handler: ChosenInlineQueryHandler.() -> Unit = {}
) : EventHandler, BotContext<ChosenInlineQueryHandler> {
    override var messageId: Int? = null
    override var inlineMessageId: String? = null

    override fun getChatId() = inlineResult.from.id
    override fun getChatIdOrThrow() = inlineResult.from.id
    override suspend fun handle() = handler()

    override fun <R> withBot(bot: Bot, block: BotContext<ChosenInlineQueryHandler>.() -> R): R {
        return ChosenInlineQueryHandler(inlineResult, bot.service, bot.fileService, handler).block()
    }
}