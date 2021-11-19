package ru.raysmith.tgbot.core.handler

import retrofit2.Response
import ru.raysmith.tgbot.core.*
import ru.raysmith.tgbot.model.network.BooleanResponse
import ru.raysmith.tgbot.model.network.CallbackQuery
import ru.raysmith.tgbot.network.TelegramApi
import ru.raysmith.tgbot.utils.DatePicker
import ru.raysmith.tgbot.utils.handleAll
import java.time.LocalDate

class CallbackQueryHandler(
    override val query: CallbackQuery,
    private val alwaysAnswer: Boolean,
    private val handlerData: Map<String, CallbackQueryHandlerData>
) : EventHandler, ISender, IEditor, BaseCallbackHandler(query) {

    override var chatId: String? = query.from.id.toString()
    override var messageId: Long? = query.message?.messageId
    override var inlineMessageId: String? = query.inlineMessageId

    companion object {
        const val HANDLER_ID = "default"
    }

    override suspend fun handle() = run {
        if (query.data == CallbackQuery.EMPTY_CALLBACK_DATA) { answer() }
        else handlerData.handleAll(this).also {
            if (alwaysAnswer && !isAnswered) answer()
        }
        Unit
    }

    fun datePickerResult(datePicker: DatePicker, datePickerHandler: DatePickerCallbackQueryHandler.(date: LocalDate) -> Unit) {
        val callbackQueryPrefix = datePicker.callbackQueryPrefix
        if (!isAnswered && query.data != null && query.data.startsWith("r$callbackQueryPrefix")) {
            val value = query.data.substring(callbackQueryPrefix.length + 1)
            DatePickerCallbackQueryHandler(query, value, callbackQueryPrefix)
                .apply { datePickerHandler(getDate()) }
                .apply { answer() }
                .also { this@CallbackQueryHandler.isAnswered = true }
        }
    }

    fun isDataEqual(value: String, equalHandler: DataCallbackQueryHandler.(data: String) -> Unit) {
        if (!isAnswered && query.data == value) {
            DataCallbackQueryHandler(query, query.data)
                .apply { equalHandler(query.data!!) }
                .apply { answer() }
                .also { this@CallbackQueryHandler.isAnswered = true }
        }
    }

    fun isDataStartWith(
        startWith: String,
        startWithHandler: ValueDataCallbackQueryHandler.(data: String, value: String) -> Unit
    ) {
        if (!isAnswered && query.data != null && query.data.startsWith(startWith)) {
            val value = query.data.substring(startWith.length)
            if (value.isEmpty()) {
                answer()
                this@CallbackQueryHandler.isAnswered = true
            } else {
                ValueDataCallbackQueryHandler(query, query.data, value)
                    .apply { startWithHandler(query.data!!, value) }
                    .apply { answer() }
                    .also { this@CallbackQueryHandler.isAnswered = true }
            }
        }
    }

    fun isUnknown(unknownHandler: UnknownQueryHandler.(query: CallbackQuery) -> Unit) {
        if (!isAnswered) {
            UnknownQueryHandler(query)
                .apply { unknownHandler(query) }
                .apply { answer() }
                .also { this@CallbackQueryHandler.isAnswered = true }
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