package ru.raysmith.tgbot.core.handler.base

import ru.raysmith.tgbot.core.Bot
import ru.raysmith.tgbot.core.BotContext
import ru.raysmith.tgbot.core.handler.EventHandler
import ru.raysmith.tgbot.core.handler.HandlerDsl
import ru.raysmith.tgbot.model.network.PollAnswer
import ru.raysmith.tgbot.network.TelegramFileService
import ru.raysmith.tgbot.network.TelegramService

@HandlerDsl
open class PollAnswerHandler(
    val pollAnswer: PollAnswer,
    override val service: TelegramService, override val fileService: TelegramFileService,
    private val handler: PollAnswerHandler.() -> Unit = {}
) : EventHandler, BotContext<PollAnswerHandler> {

    override fun getChatId() = null
    override var messageId: Int? = null
    override var inlineMessageId: String? = null

    override suspend fun handle() = handler()

    override fun <R> withBot(bot: Bot, block: BotContext<PollAnswerHandler>.() -> R): R {
        return PollAnswerHandler(pollAnswer, bot.service, bot.fileService, handler).block()
    }
}