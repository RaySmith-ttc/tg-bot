package ru.raysmith.tgbot.core

import ru.raysmith.tgbot.utils.getOrDefault
import ru.raysmith.utils.properties.getOrNull
import java.util.*

/** Contains customizable bot options */
class BotConfig {
    var safeTextLength = Bot.properties.getOrDefault("safeTextLength", "true").toBoolean()
    // TODO [~] add sendSequentiallyLargeMessage: Boolean for sending 3 message for 10.000 symbols message
    var printNulls = Bot.properties.getOrDefault("printNulls", "false").toBoolean()
    var defaultProviderToken = Bot.properties?.getOrNull("providerToken")
    var emptyCallbackQuery = Bot.properties.getOrDefault("emptyCallbackQuery", " ")
    var token = Bot.properties?.getOrNull("token") ?: System.getenv("TG_BOT_TOKEN") ?: System.getenv("BOT_TOKEN") // TODO [stable] remove second
    
    var defaultRows: Int = Bot.properties.getOrDefault("pagination.rows", "5").toIntOrNull()
        ?: throw IllegalArgumentException("Property pagination.rows is not Int")
    var defaultColumns: Int = Bot.properties.getOrDefault("pagination.columns", "1").toIntOrNull()
        ?: throw IllegalArgumentException("Property pagination.columns is not Int")
    var firstPageSymbol = Bot.properties.getOrDefault("pagination.firstPageSymbol", "«")
    var lastPageSymbol = Bot.properties.getOrDefault("pagination.lastPageSymbol", "»")
    var locale = Bot.properties?.getOrNull("calendar_locale")?.let {
        Locale.forLanguageTag(it)
    } ?: Locale.getDefault()

    var alwaysAnswerCallback: Boolean = false
}