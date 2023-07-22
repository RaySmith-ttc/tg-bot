package ru.raysmith.tgbot.core.handler.location

import ru.raysmith.tgbot.core.*
import ru.raysmith.tgbot.core.handler.HandlerDsl
import ru.raysmith.tgbot.core.handler.LocationHandler
import ru.raysmith.tgbot.core.handler.base.CallbackQueryHandler
import ru.raysmith.tgbot.model.network.CallbackQuery
import ru.raysmith.tgbot.model.network.updates.Update
import ru.raysmith.tgbot.network.TelegramFileService
import ru.raysmith.tgbot.network.TelegramService
import ru.raysmith.tgbot.utils.datepicker.DatePicker
import ru.raysmith.tgbot.utils.locations.LocationConfig
import ru.raysmith.tgbot.utils.locations.LocationsWrapper

data class LocationCallbackQueryHandlerData<T : LocationConfig>(
    val handler: (context(T) LocationCallbackQueryHandler<T>.() -> Unit)? = null,
    val datePicker: DatePicker? = null,
    val alwaysAnswer: Boolean
)

@HandlerDsl
open class LocationCallbackQueryHandler<T : LocationConfig>(
    override val update: Update, service: TelegramService, fileService: TelegramFileService,
    private val handlerData: Map<String, LocationCallbackQueryHandlerData<T>>,
    override val locationsWrapper: LocationsWrapper<T>
) : CallbackQueryHandler(update.callbackQuery!!, emptyMap(), service, fileService), LocationHandler<T> {
    
    override val config by lazy { config() }
    override suspend fun handle() {
        if (query.data == CallbackQuery.EMPTY_CALLBACK_DATA) { answer() }

        for (data in handlerData) {
            if (isAnswered) {
                break
            }
    
            data.value.datePicker?.handle(this) ?: data.value.handler?.let { h -> h(config, this) }
        }

        if (!isAnswered && handlerData.any { it.value.alwaysAnswer }) {
            answer()
        }
    }
    override fun <R> withBot(bot: Bot, block: BotContext<CallbackQueryHandler>.() -> R): R {
        return LocationCallbackQueryHandler(update, bot.service, bot.fileService, handlerData, locationsWrapper).block()
    }
}