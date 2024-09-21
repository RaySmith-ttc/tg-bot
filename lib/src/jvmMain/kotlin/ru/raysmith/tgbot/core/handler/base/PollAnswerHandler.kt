package ru.raysmith.tgbot.core.handler.base

import io.ktor.client.*
import ru.raysmith.tgbot.core.Bot
import ru.raysmith.tgbot.core.BotConfig
import ru.raysmith.tgbot.core.BotContext
import ru.raysmith.tgbot.core.handler.BaseEventHandler
import ru.raysmith.tgbot.core.handler.HandlerDsl
import ru.raysmith.tgbot.exceptions.UnknownChatIdException
import ru.raysmith.tgbot.model.network.PollAnswer

@HandlerDsl
open class PollAnswerHandler(
    val pollAnswer: PollAnswer,
    final override val bot: Bot,
    private val handler: suspend PollAnswerHandler.() -> Unit = {}
) : BaseEventHandler(), BotContext<PollAnswerHandler> {
    override val client: HttpClient = bot.client
    override val botConfig: BotConfig = bot.botConfig

    override var messageId: Int? = null
    override var inlineMessageId: String? = null

    override fun getChatId() = pollAnswer.voterChat?.id
    override fun getChatIdOrThrow() = pollAnswer.voterChat?.id ?: throw UnknownChatIdException()

    override suspend fun handle() {
        handler()
        handled = true
        handleLocalFeatures(handled)
    }

    override suspend fun <R> withBot(bot: Bot, block: suspend BotContext<PollAnswerHandler>.() -> R): R {
        return PollAnswerHandler(pollAnswer, bot, handler).block()
    }
}