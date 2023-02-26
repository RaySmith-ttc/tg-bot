package ru.raysmith.tgbot.core.handler.location

import ru.raysmith.tgbot.core.*
import ru.raysmith.tgbot.core.handler.CallbackQueryHandler
import ru.raysmith.tgbot.model.network.CallbackQuery
import ru.raysmith.tgbot.model.network.updates.Update
import ru.raysmith.tgbot.network.TelegramFileService
import ru.raysmith.tgbot.network.TelegramService
import ru.raysmith.tgbot.utils.locations.LocationConfig
import ru.raysmith.tgbot.utils.locations.LocationsWrapper

@HandlerDsl
open class LocationCallbackQueryHandler<T : LocationConfig>(
    override val update: Update, service: TelegramService, fileService: TelegramFileService, alwaysAnswer: Boolean,
    private val handlerData: Map<String, LocationCallbackQueryHandlerData<T>>,
    override val locationsWrapper: LocationsWrapper<T>
) : CallbackQueryHandler(update.callbackQuery!!, alwaysAnswer, emptyMap(), service, fileService), LocationHandler<T> {
    
    override fun toLocation(name: String) = defaultToLocation(name)
    override suspend fun handle() {
        if (query.data == CallbackQuery.EMPTY_CALLBACK_DATA) { answer() }
    
        for (data in handlerData) {
            if (isAnswered) break
    
            data.value.datePicker?.handle(this) ?: data.value.handler?.let { it1 -> it1(config()) }
        }
    }
    override fun withBot(bot: Bot, block: BotContext<CallbackQueryHandler>.() -> Any) {
        LocationCallbackQueryHandler(update, service, fileService, alwaysAnswer, handlerData, locationsWrapper).apply {
            this.block()
        }
    }
}