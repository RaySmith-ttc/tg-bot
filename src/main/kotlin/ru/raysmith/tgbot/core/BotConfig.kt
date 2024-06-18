package ru.raysmith.tgbot.core

import ru.raysmith.tgbot.model.network.message.LinkPreviewOptions
import ru.raysmith.tgbot.utils.BotFeature
import ru.raysmith.tgbot.utils.getOrDefault
import ru.raysmith.tgbot.utils.pagination.DefaultPaginationFetcherFactory
import ru.raysmith.tgbot.utils.pagination.PaginationFetcherFactory
import ru.raysmith.utils.properties.getOrNull
import java.util.*

/** Contains customizable bot options */
class BotConfig {
    companion object {
        val default = BotConfig()
    }

    var verifyMarkdown2Format = Bot.properties.getOrDefault("verifyMarkdown2Format", "true").toBoolean()
    var safeTextLength = Bot.properties.getOrDefault("safeTextLength", "true").toBoolean()
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
    var locale = Bot.properties?.getOrNull("calendar_locale")?.let {
        Locale.forLanguageTag(it)
    } ?: Locale.getDefault()

    var alwaysAnswerCallback: Boolean = false
    var ignoreEmptyCallbackData: Boolean = true

    var sendChatActionWithMedaMessage: Boolean = false

    /**
     * Instance of [PaginationFetcherFactory]. It describes how to fetch page data from all data in pagination.
     * For example, you may want to change default fetch logic when use _SizedIterable from kotlin exposed library_
     * to add limit to query
     * */
    var paginationFetcherFactory: PaginationFetcherFactory = DefaultPaginationFetcherFactory()

    var defaultCallbackQueryHandlerFeatures: List<BotFeature> = emptyList()
    var linkPreviewOptions: LinkPreviewOptions? = null
}