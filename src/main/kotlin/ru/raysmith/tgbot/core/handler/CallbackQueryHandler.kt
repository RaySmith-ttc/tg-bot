package ru.raysmith.tgbot.core.handler

import org.slf4j.LoggerFactory
import ru.raysmith.tgbot.core.Bot
import ru.raysmith.tgbot.core.BotContext
import ru.raysmith.tgbot.core.EventHandler
import ru.raysmith.tgbot.core.HandlerDsl
import ru.raysmith.tgbot.model.network.CallbackQuery
import ru.raysmith.tgbot.network.TelegramFileService
import ru.raysmith.tgbot.network.TelegramService
import ru.raysmith.tgbot.utils.datepicker.DatePicker
import ru.raysmith.tgbot.utils.handleAll
import java.time.LocalDate

@HandlerDsl
class CallbackQueryHandler(
    override val query: CallbackQuery,
    private val alwaysAnswer: Boolean,
    private val handlerData: Map<String, CallbackQueryHandlerData>,
    override val service: TelegramService, override val fileService: TelegramFileService
) : EventHandler, BaseCallbackHandler(query, service, fileService), BotContext<CallbackQueryHandler> {

    override fun getChatId() = query.message?.chat?.id
    override var messageId: Int? = query.message?.messageId
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
            DatePickerCallbackQueryHandler(query, value, callbackQueryPrefix, service, fileService)
                .apply { datePickerHandler(getDate()) }
        }
    }

    fun datePickerResultWithAdditionalData(
        datePicker: DatePicker,
        datePickerHandler: DatePickerCallbackQueryHandler.(date: LocalDate, additionalData: String?) -> Unit
    ) {
        val callbackQueryPrefix = datePicker.callbackQueryPrefix
        if (!isAnswered && query.data != null && query.data.startsWith("r$callbackQueryPrefix")) {
            val value = query.data.substring(callbackQueryPrefix.length + 1)
            DatePickerCallbackQueryHandler(query, value, callbackQueryPrefix, service, fileService)
                .apply { datePickerHandler(getDate(), datePickerData.additionalData) }
        }
    }

    fun isDataEqual(value: String, equalHandler: DataCallbackQueryHandler.(data: String) -> Unit) {
        if (!isAnswered && query.data == value) {
            DataCallbackQueryHandler(query, query.data, service, fileService)
                .apply { equalHandler(query.data!!) }
        }
    }

    fun isPage(paginationCallbackQueryPrefix: String, handler: PaginationCallbackQueryHandler.(page: Long) -> Unit) {
        if (!isAnswered && query.data != null && query.data.startsWith(paginationCallbackQueryPrefix)) {
            query.data.substring(paginationCallbackQueryPrefix.length).let {
                // 1 = Pagination.SYMBOL_PAGE_PREFIX length
                if (it.length <= 1) null
                else it.substring(1).toLongOrNull()?.let { page ->
                    PaginationCallbackQueryHandler(query, page, service, fileService).apply { handler(page) }
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
                ValueDataCallbackQueryHandler(query, value, service, fileService)
                    .apply { startWithHandler(value) }
            }
        }
    }

    // TODO ???????????? ?????????? ?????????? ??????????????, ??.??. ???? ???????????????????? ???????????????? ?? ???? ?????????? ???????? ???????????? ???? ??????????????
    //  ?????????????????????? ?????????????????????? ???????? ???? ???????????? (default -> datePiker1 -> datePiker2 -> ...) ?? ?? ???????????? ???????? ?????????????????????? ????????????????????
    @Deprecated("Not work correct. Deleted soon", replaceWith = ReplaceWith(""))
    fun isUnhandled(unknownHandler: UnknownQueryHandler.(query: CallbackQuery) -> Unit) {
        if (!isAnswered) {
            UnknownQueryHandler(query)
                .apply { unknownHandler(query) }
        }
    }

    override fun withBot(bot: Bot, block: BotContext<CallbackQueryHandler>.() -> Any) {
        CallbackQueryHandler(query, alwaysAnswer, handlerData, service, fileService).apply {
            block()
        }
    }
}