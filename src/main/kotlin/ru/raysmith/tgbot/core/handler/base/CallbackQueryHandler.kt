package ru.raysmith.tgbot.core.handler.base

import io.ktor.client.*
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import ru.raysmith.tgbot.core.Bot
import ru.raysmith.tgbot.core.BotConfig
import ru.raysmith.tgbot.core.BotContext
import ru.raysmith.tgbot.core.handler.EventHandler
import ru.raysmith.tgbot.core.handler.HandlerDsl
import ru.raysmith.tgbot.core.handler.utils.DataCallbackQueryHandler
import ru.raysmith.tgbot.core.handler.utils.ValueDataCallbackQueryHandler
import ru.raysmith.tgbot.exceptions.UnknownChatIdException
import ru.raysmith.tgbot.model.network.CallbackQuery
import ru.raysmith.tgbot.utils.BotFeature
import ru.raysmith.tgbot.utils.datepicker.DatePicker
import java.time.LocalDate

data class CallbackQueryHandlerData(
    val handler: (suspend (CallbackQueryHandler.() -> Unit))? = null,
    val alwaysAnswer: Boolean
)

@HandlerDsl
open class CallbackQueryHandler(
    final override val query: CallbackQuery,
    private val handlerData: Map<String, CallbackQueryHandlerData>,
    final override val bot: Bot
) : EventHandler, BaseCallbackHandler(query, bot.client), BotContext<CallbackQueryHandler> {

    @Suppress("MemberVisibilityCanBePrivate")
    protected val localFeatures: MutableList<BotFeature> = mutableListOf()

    override val client: HttpClient = bot.client
    override val botConfig: BotConfig = bot.botConfig
    override fun getChatId() = query.message?.chat?.id
    override fun getChatIdOrThrow() = query.message?.chat?.id ?: throw UnknownChatIdException()
    override var messageId: Int? = query.message?.messageId
    override var inlineMessageId: String? = query.inlineMessageId

    companion object {
        const val HANDLER_ID = "default"
        const val GLOBAL_HANDLER_ID = "global"
        val logger: Logger = LoggerFactory.getLogger("callback-query-handler-$HANDLER_ID")
    }

    init {
        localFeatures.addAll(bot.botConfig.defaultCallbackQueryHandlerFeatures.toTypedArray())
    }

    override suspend fun setupFeatures(vararg features: BotFeature, callFirst: Boolean) {
        if (callFirst) localFeatures.addAll(0, features.toList())
        else localFeatures.addAll(features)
    }

    context(EventHandler)
    protected suspend fun handleLocalFeatures(handled: Boolean) {
        localFeatures.forEach { feature ->
            feature.handle(this as EventHandler, handled)
        }
    }

    override suspend fun handle() {
        if (query.data == CallbackQuery.EMPTY_CALLBACK_DATA && bot.botConfig.ignoreEmptyCallbackData) { answer() }
        else {
            for (data in handlerData) {
                if (handled) {
                    break
                }

                data.value.handler?.invoke(this)
            }

            handleLocalFeatures(handled)

            if (!handled && handlerData.any { it.value.alwaysAnswer }) {
                answer()
            }
        }
    }

    suspend fun datePickerResult(datePicker: DatePicker, datePickerHandler: suspend DatePickerCallbackQueryHandler.(date: LocalDate) -> Unit) {
        val callbackQueryPrefix = datePicker.callbackQueryPrefix
        if (!handled && query.data != null && query.data.startsWith("r$callbackQueryPrefix")) {
            val value = query.data.substring(callbackQueryPrefix.length + 1)
            DatePickerCallbackQueryHandler(query, value, callbackQueryPrefix, bot)
                .apply { datePickerHandler(getDate()) }
            handled = true
        }
    }

    suspend fun datePickerResultWithAdditionalData(
        datePicker: DatePicker,
        datePickerHandler: suspend DatePickerCallbackQueryHandler.(date: LocalDate, additionalData: String?) -> Unit
    ) {
        val callbackQueryPrefix = datePicker.callbackQueryPrefix
        if (!handled && query.data != null && query.data.startsWith("r$callbackQueryPrefix")) {
            val value = query.data.substring(callbackQueryPrefix.length + 1)
            DatePickerCallbackQueryHandler(query, value, callbackQueryPrefix, bot)
                .apply { datePickerHandler(getDate(), datePickerData.additionalData) }
            handled = true
        }
    }

    suspend fun isDataEqual(vararg value: String, equalHandler: suspend DataCallbackQueryHandler.(data: String) -> Unit) {
        if (!handled && query.data != null && query.data in value) {
            DataCallbackQueryHandler(query, query.data, bot)
                .apply { equalHandler(query.data!!) }
            handled = true
        }
    }

    suspend fun isDataRegex(regex: Regex, regexHandler: suspend DataCallbackQueryHandler.(value: String) -> Unit) {
        if (!handled && query.data?.matches(regex) == true) {
            DataCallbackQueryHandler(query, query.data, bot)
                .apply { regexHandler(query.data!!) }
            handled = true
        }
    }

    suspend fun isPage(paginationCallbackQueryPrefix: String, handler: suspend PaginationCallbackQueryHandler.(page: Int) -> Unit) {
        if (!handled && query.data != null && query.data.startsWith(paginationCallbackQueryPrefix)) {
            query.data.substring(paginationCallbackQueryPrefix.length).let {
                // 1 = Pagination.SYMBOL_PAGE_PREFIX length
                if (it.length <= 1) null
                else it.substring(1).toIntOrNull()?.let { page ->
                    PaginationCallbackQueryHandler(query, page, bot).apply { handler(page) }
                }
            } ?: logger.warn("Pagination data incorrect. Are you sure '$paginationCallbackQueryPrefix' prefix should use isPage handler?")
            handled = true
        }
    }

    suspend fun isDataStartWith(
        vararg startWith: String,
        startWithHandler: suspend ValueDataCallbackQueryHandler.(value: String) -> Unit
    ) {
        for (startWithEntry in startWith) {
            if (!handled && query.data != null && query.data.startsWith(startWithEntry)) {
                val value = query.data.substring(startWithEntry.length)
                if (value != CallbackQuery.EMPTY_CALLBACK_DATA || !bot.botConfig.ignoreEmptyCallbackData) {
                    ValueDataCallbackQueryHandler(query, value, bot)
                        .apply { startWithHandler(value) }
                    handled = true
                    break
                }
            }
        }
    }

    override suspend fun <R> withBot(bot: Bot, block: suspend BotContext<CallbackQueryHandler>.() -> R): R {
        return CallbackQueryHandler(query, handlerData, bot).block()
    }
}