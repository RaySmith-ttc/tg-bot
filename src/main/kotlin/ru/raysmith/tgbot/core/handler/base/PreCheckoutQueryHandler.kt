package ru.raysmith.tgbot.core.handler.base

import ru.raysmith.tgbot.core.Bot
import ru.raysmith.tgbot.core.BotContext
import ru.raysmith.tgbot.core.handler.EventHandler
import ru.raysmith.tgbot.core.handler.HandlerDsl
import ru.raysmith.tgbot.model.network.payment.PreCheckoutQuery
import ru.raysmith.tgbot.network.TelegramFileService
import ru.raysmith.tgbot.network.TelegramService
import ru.raysmith.tgbot.utils.errorBody

@HandlerDsl
open class PreCheckoutQueryHandler(
    val preCheckoutQuery: PreCheckoutQuery,
    override val service: TelegramService, override val fileService: TelegramFileService,
    private val handler: PreCheckoutQueryHandler.() -> Unit = {}
) : EventHandler, BotContext<PreCheckoutQueryHandler> {

    override fun getChatId() = preCheckoutQuery.from.id
    override var messageId: Int? = null
    override var inlineMessageId: String? = null

    override suspend fun handle() = handler()

    override fun <R> withBot(bot: Bot, block: BotContext<PreCheckoutQueryHandler>.() -> R): R {
        return PreCheckoutQueryHandler(preCheckoutQuery, bot.service, bot.fileService, handler).let {
            this.block()
        }
    }

    fun answerPreCheckoutQuery(ok: Boolean, errorMessage: String? = null): Boolean {
        return service.answerPreCheckoutQuery(
            preCheckoutQuery.id, ok, errorMessage
        ).execute().body()?.result ?: errorBody()
    }
}