package ru.raysmith.tgbot.utils

import kotlinx.serialization.KSerializer
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import ru.raysmith.tgbot.core.ApiCaller
import ru.raysmith.tgbot.core.Bot
import ru.raysmith.tgbot.core.BotContext
import ru.raysmith.tgbot.core.EventHandler
import ru.raysmith.tgbot.core.handler.UnknownEventHandler
import ru.raysmith.tgbot.model.bot.ChatId
import ru.raysmith.tgbot.model.network.updates.Update
import ru.raysmith.tgbot.network.TelegramApi
import ru.raysmith.tgbot.network.TelegramFileService
import ru.raysmith.tgbot.network.TelegramService

// should never be called
internal fun errorBody(): Nothing = throw NullPointerException("The method did not return a body")
internal fun KSerializer<*>.fieldNotFound(filed: String): Nothing =
    error("The ${descriptor.serialName} json object must contains '$filed'")
internal fun KSerializer<*>.getPrimitive(element: JsonObject, field: String) =
    element.jsonObject[field]?.jsonPrimitive?.content ?: fieldNotFound(field)
internal fun KSerializer<*>.getJsonObject(element: JsonObject, field: String) =
    element.jsonObject[field]?.jsonObject ?: fieldNotFound(field)

fun botContext(bot: Bot, withChatId: ChatId? = null) = createBotContext(bot.service, bot.fileService, withChatId)
fun botContext(token: String, withChatId: ChatId? = null) = createBotContext(
    TelegramApi.serviceWithToken(token), TelegramApi.fileServiceWithToken(token), withChatId
)

/**
 * Creates a bot context and executes a [block] that can call API requests
 *
 * @param bot Telegram service with a bot token
 * @param withChatId Unique identifier for the target chat or username of the target channel (in the format `@channelusername`)
 * */
@BotContextDsl
inline fun <T> botContext(bot: Bot, withChatId: ChatId? = null, block: BotContext<UnknownEventHandler>.() -> T) =
    botContext(bot.service, bot.fileService, withChatId, block)

/**
 * Creates a bot context and executes a [block] that can call API requests
 *
 * @param token Telegram bot token
 * @param withChatId Unique identifier for the target chat or username of the target channel (in the format `@channelusername`)
 * */
@BotContextDsl
inline fun <T> botContext(token: String, withChatId: ChatId? = null, block: BotContext<UnknownEventHandler>.() -> T) =
    botContext(TelegramApi.serviceWithToken(token), TelegramApi.fileServiceWithToken(token), withChatId, block)

/**
 * Creates a bot context and executes a [block] that can call API requests
 *
 * @param withChatId Unique identifier for the target chat or username of the target channel (in the format `@channelusername`)
 * @param service Telegram service with a bot token
 * */
@BotContextDsl
inline fun <T> botContext(
    service: TelegramService = TelegramApi.service, fileService: TelegramFileService = TelegramApi.fileService,
    withChatId: ChatId? = null,
    block: BotContext<UnknownEventHandler>.() -> T
) = createBotContext(service, fileService, withChatId).let(block)

fun createBotContext(
    service: TelegramService = TelegramApi.service, fileService: TelegramFileService = TelegramApi.fileService,
    withChatId: ChatId? = null,
) = object : BotContext<UnknownEventHandler> {
    override val service: TelegramService = service
    override val fileService: TelegramFileService = fileService

    override fun getChatId(): ChatId? = withChatId
    override fun withBot(bot: Bot, block: BotContext<UnknownEventHandler>.() -> Any) {
        UnknownEventHandler(Update(-1), service, fileService).apply {
            block()
        }
    }
}

@DslMarker
annotation class BotContextDsl

internal fun noimpl(): Nothing = throw NotImplementedError()

/**
 * Calls [getMe][ApiCaller.getMe] method for current bot in context and
 * returns sticker name with base [name] appended with `_by_<bot_username>`
 * */
fun <T : EventHandler> BotContext<T>.stickerSetName(name: String) = "${name}_by_${getMe().username}"