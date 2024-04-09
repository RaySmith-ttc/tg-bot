package ru.raysmith.tgbot.core

import ru.raysmith.tgbot.utils.datepicker.BotFeature
import ru.raysmith.tgbot.utils.getOrDefault
import ru.raysmith.tgbot.utils.pagination.DefaultPaginationFetcherFactory
import ru.raysmith.tgbot.utils.pagination.PaginationFetcherFactory
import ru.raysmith.utils.properties.getOrNull

// TODO add global disable links preview

/** Contains customizable bot options */
class BotConfig {
    companion object {
        val default = BotConfig()
    }

    var safeTextLength = Bot.properties.getOrDefault("safeTextLength", "true").toBoolean()
    // TODO [~] add sendSequentiallyLargeMessage: Boolean for sending 3 message for 10.000 symbols message
    var printNulls = Bot.properties.getOrDefault("printNulls", "false").toBoolean()
    var defaultProviderToken = Bot.properties?.getOrNull("providerToken")
//    var emptyCallbackQuery = Bot.properties.getOrDefault("emptyCallbackQuery", " ") // TODO [docs] can't be obtain from  config
    var token = Bot.properties?.getOrNull("token") ?: System.getenv("TG_BOT_TOKEN") ?: System.getenv("BOT_TOKEN") // TODO [stable] remove second
    
    var defaultRows: Int = Bot.properties.getOrDefault("pagination.rows", "5").toIntOrNull()
        ?: throw IllegalArgumentException("Property pagination.rows is not Int")
    var defaultColumns: Int = Bot.properties.getOrDefault("pagination.columns", "1").toIntOrNull()
        ?: throw IllegalArgumentException("Property pagination.columns is not Int")
    var firstPageSymbol = Bot.properties.getOrDefault("pagination.firstPageSymbol", "«")
    var lastPageSymbol = Bot.properties.getOrDefault("pagination.lastPageSymbol", "»")
//    var locale = Bot.properties?.getOrNull("calendar_locale")?.let { // TODO [docs] can't be obtain from  config
//        Locale.forLanguageTag(it)
//    } ?: Locale.getDefault()

    var alwaysAnswerCallback: Boolean = false
    var ignoreEmptyCallbackData: Boolean = true

    var sendChatActionWithMedaMessage: Boolean = false
    var paginationFetcherFactory: PaginationFetcherFactory = DefaultPaginationFetcherFactory()

    var defaultCallbackQueryHandlerFeatures: List<BotFeature> = emptyList()
    var disableWebPagePreviews: Boolean? = null
}