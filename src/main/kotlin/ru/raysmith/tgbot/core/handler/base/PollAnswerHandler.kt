package ru.raysmith.tgbot.core.handler.base

import io.ktor.client.*
import ru.raysmith.tgbot.core.Bot
import ru.raysmith.tgbot.core.BotContext
import ru.raysmith.tgbot.core.handler.BaseEventHandler
import ru.raysmith.tgbot.core.handler.HandlerDsl
import ru.raysmith.tgbot.model.network.PollAnswer

@HandlerDsl
open class PollAnswerHandler(
    val pollAnswer: PollAnswer,
    override val client: HttpClient,
    private val handler: suspend PollAnswerHandler.() -> Unit = {}
) : BaseEventHandler(), BotContext<PollAnswerHandler> {

    override fun getChatId() = null
    override var messageId: Int? = null
    override var inlineMessageId: String? = null

    override suspend fun handle() {
        handler()
        handled = true
        handleLocalFeatures(handled)
    }

    override suspend fun <R> withBot(bot: Bot, block: suspend BotContext<PollAnswerHandler>.() -> R): R {
        return PollAnswerHandler(pollAnswer, bot.client, handler).block()
    }
}