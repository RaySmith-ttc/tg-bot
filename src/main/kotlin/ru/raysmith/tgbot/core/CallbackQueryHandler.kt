package ru.raysmith.tgbot.core

import retrofit2.Response
import ru.raysmith.tgbot.model.bot.TextMessage
import ru.raysmith.tgbot.model.network.BooleanResponse
import ru.raysmith.tgbot.model.network.CallbackQuery
import ru.raysmith.tgbot.model.network.keyboard.InlineKeyboardButton
import ru.raysmith.tgbot.model.network.message.response.MessageSendResponse
import ru.raysmith.tgbot.network.TelegramApi

class CallbackQueryHandler(
    override val query: CallbackQuery,
    private val alwaysAnswer: Boolean,
    private val handler: CallbackQueryHandler.() -> Unit
): EventHandler, ISender, BaseCallbackHandler(query) {

    override var chatId: String? = query.from.id.toString()
    override var messageId: Long? = query.message?.messageId
    override var inlineMessageId: String? = query.inlineMessageId

    override suspend fun handle() = run {
        if (query.data == InlineKeyboardButton.EMPTY_CALLBACK_DATA) { answer() }
        else handler().apply {
            if (alwaysAnswer && !isAnswered) answer()
        }
        Unit
    }

    fun isDataEqual(value: String, equalHandler: DataCallbackQueryHandler.(data: String) -> Unit) {
        if (!isAnswered && query.data == value) {
            DataCallbackQueryHandler(query, query.data).apply { equalHandler(query.data!!) }.apply { answer() }
        }
    }

    fun isDataStartWith(startWith: String, startWithHandler: ValueDataCallbackQueryHandler.(data: String, value: String) -> Unit) {
        if (!isAnswered && query.data != null && query.data.startsWith(startWith)) {
            val value = query.data.substring(startWith.length, query.data.length)
            ValueDataCallbackQueryHandler(query, query.data, value).apply { startWithHandler(query.data!!, value) }.apply { answer() }
        }
    }

    fun isUnknown(unknownHandler: UnknownQueryHandler.(query: CallbackQuery) -> Unit) {
        if (!isAnswered) {
            UnknownQueryHandler(query).apply { unknownHandler(query) }
        }
    }

    override fun answer(init: AnswerCallbackQuery.() -> Unit): Response<BooleanResponse> {
        return AnswerCallbackQuery().apply(init).let {
            TelegramApi.service.answerCallbackQuery(
                callbackQueryId = query.id,
                text = it.text,
                showAlert = it.showAlert,
                url = it.url,
                cacheTime = it.cacheTime
            ).execute().also { response ->
                if (response.isSuccessful && response.body()?.result == true) {
                    isAnswered = true
                }
            }
        }

    }
}