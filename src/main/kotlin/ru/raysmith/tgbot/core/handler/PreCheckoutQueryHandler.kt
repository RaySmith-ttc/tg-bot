package ru.raysmith.tgbot.core.handler

import ru.raysmith.tgbot.core.Bot
import ru.raysmith.tgbot.core.BotContext
import ru.raysmith.tgbot.core.EventHandler
import ru.raysmith.tgbot.core.HandlerDsl
import ru.raysmith.tgbot.model.network.payment.PreCheckoutQuery
import ru.raysmith.tgbot.network.TelegramApi
import ru.raysmith.tgbot.network.TelegramService
import ru.raysmith.tgbot.utils.errorBody

@HandlerDsl
class PreCheckoutQueryHandler(
    val preCheckoutQuery: PreCheckoutQuery,
    private val handler: PreCheckoutQueryHandler.() -> Unit
) : EventHandler, BotContext<PreCheckoutQueryHandler> {

    override fun getChatId() = preCheckoutQuery.from.id.toString()
    override var messageId: Int? = null
    override var inlineMessageId: String? = null

    override suspend fun handle() = handler()

    override var service: TelegramService = TelegramApi.service
    override fun withBot(bot: Bot, block: BotContext<PreCheckoutQueryHandler>.() -> Any) {
        PreCheckoutQueryHandler(preCheckoutQuery, handler).apply {
            this.service = bot.service
            this.block()
        }
    }

    fun answerPreCheckoutQuery(ok: Boolean, errorMessage: String? = null): Boolean {
        return TelegramApi.service.answerPreCheckoutQuery(
            preCheckoutQuery.id, ok, errorMessage
        ).execute().body()?.result ?: errorBody()
    }
}