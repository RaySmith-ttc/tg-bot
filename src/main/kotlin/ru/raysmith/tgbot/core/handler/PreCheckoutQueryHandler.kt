package ru.raysmith.tgbot.core.handler

import ru.raysmith.tgbot.core.EventHandler
import ru.raysmith.tgbot.core.IEditor
import ru.raysmith.tgbot.core.ISender
import ru.raysmith.tgbot.model.network.payment.PreCheckoutQuery
import ru.raysmith.tgbot.network.TelegramApi

class PreCheckoutQueryHandler(
    val preCheckoutQuery: PreCheckoutQuery,
    private val handler: PreCheckoutQueryHandler.() -> Unit
) :  EventHandler, ISender, IEditor {

    override suspend fun handle() = handler()
    override var chatId: String? = preCheckoutQuery.from.id.toString()
    override var messageId: Long? = null
    override var inlineMessageId: String? = null

    fun answerPreCheckoutQuery(ok: Boolean, errorMessage: String? = null): Boolean {
        return TelegramApi.service.answerPreCheckoutQuery(
            preCheckoutQuery.id, ok, errorMessage
        ).execute().body()!!.result
    }
}