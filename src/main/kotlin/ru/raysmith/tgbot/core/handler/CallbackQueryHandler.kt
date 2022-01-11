package ru.raysmith.tgbot.core.handler

import org.slf4j.LoggerFactory
import retrofit2.Response
import ru.raysmith.tgbot.core.*
import ru.raysmith.tgbot.model.network.BooleanResponse
import ru.raysmith.tgbot.model.network.CallbackQuery
import ru.raysmith.tgbot.network.TelegramApi
import ru.raysmith.tgbot.utils.datepicker.DatePicker
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
        val logger = LoggerFactory.getLogger("callback-query-handler-$HANDLER_ID")
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
        }
    }

    fun isDataEqual(value: String, equalHandler: DataCallbackQueryHandler.(data: String) -> Unit) {
        if (!isAnswered && query.data == value) {
            DataCallbackQueryHandler(query, query.data)
                .apply { equalHandler(query.data!!) }
        }
    }

    fun isPage(paginationCallbackQueryPrefix: String, handler: PaginationCallbackQueryHandler.(page: Long) -> Unit) {
        if (!isAnswered && query.data != null && query.data.startsWith(paginationCallbackQueryPrefix)) {
            query.data.substring(paginationCallbackQueryPrefix.length).let {
                if (it.isEmpty()) null
                else it.substring(1).toLongOrNull()?.let { page ->
                    PaginationCallbackQueryHandler(query, page).apply { handler(page) }
                }
            } ?: logger.warn("Pagination data incorrect. Are you sure '$paginationCallbackQueryPrefix' prefix should use isPage handler?")
        }
    }

    fun isDataStartWith(
        startWith: String,
        startWithHandler: ValueDataCallbackQueryHandler.(value: String) -> Unit
    ) {
        if (!isAnswered && query.data != null && query.data.startsWith(startWith)) {
            val value = query.data.substring(startWith.length)
            if (value != CallbackQuery.EMPTY_CALLBACK_DATA) {
                ValueDataCallbackQueryHandler(query, value)
                    .apply { startWithHandler(value) }
            }
        }
    }

    fun isUnhandled(unknownHandler: UnknownQueryHandler.(query: CallbackQuery) -> Unit) {
        if (!isAnswered) {
            UnknownQueryHandler(query)
                .apply { unknownHandler(query) }
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