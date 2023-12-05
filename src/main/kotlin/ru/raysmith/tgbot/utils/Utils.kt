package ru.raysmith.tgbot.utils

import io.ktor.client.*
import kotlinx.serialization.KSerializer
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import ru.raysmith.tgbot.core.Bot
import ru.raysmith.tgbot.core.BotContext
import ru.raysmith.tgbot.core.handler.EventHandler
import ru.raysmith.tgbot.core.handler.base.UnknownEventHandler
import ru.raysmith.tgbot.model.bot.ChatId
import ru.raysmith.tgbot.model.network.updates.Update
import ru.raysmith.tgbot.network.API
import ru.raysmith.tgbot.network.TelegramApi

// should never be called
internal fun errorBody(): Nothing = throw NullPointerException("The method did not return a body")
internal fun KSerializer<*>.fieldNotFound(filed: String): Nothing =
    error("The ${descriptor.serialName} json object must contains '$filed'")
internal fun KSerializer<*>.getPrimitive(element: JsonObject, field: String) =
    element.jsonObject[field]?.jsonPrimitive?.content ?: fieldNotFound(field)
internal fun KSerializer<*>.getJsonObject(element: JsonObject, field: String) =
    element.jsonObject[field]?.jsonObject ?: fieldNotFound(field)

fun botContext(bot: Bot, withChatId: ChatId? = null) = createBotContext(bot.client, withChatId)
fun botContext(token: String, withChatId: ChatId? = null) =
    createBotContext(TelegramApi.defaultClient(token), withChatId)

/**
 * Creates a bot context and executes a [block] that can call API requests
 *
 * @param bot Telegram service with a bot token
 * @param withChatId Unique identifier for the target chat or username of the target channel (in the format `@channelusername`)
 * */
@BotContextDsl
suspend inline fun <T> botContext(bot: Bot, withChatId: ChatId? = null, crossinline block: suspend BotContext<UnknownEventHandler>.() -> T) =
    botContext(bot.client, withChatId, block)

/**
 * Creates a bot context and executes a [block] that can call API requests
 *
 * @param token Telegram bot token
 * @param withChatId Unique identifier for the target chat or username of the target channel (in the format `@channelusername`)
 * */
@BotContextDsl
suspend inline fun <T> botContext(token: String, withChatId: ChatId? = null, crossinline block: suspend BotContext<UnknownEventHandler>.() -> T) =
    botContext(TelegramApi.defaultClient(token), withChatId, block)

/**
 * Creates a bot context and executes a [block] that can call API requests
 *
 * @param withChatId Unique identifier for the target chat or username of the target channel (in the format `@channelusername`)
 * */
@BotContextDsl
suspend inline fun <T> botContext(
    client: HttpClient = TelegramApi.defaultClientInstance,
    withChatId: ChatId? = null,
    crossinline block: suspend BotContext<UnknownEventHandler>.() -> T
) = block(createBotContext(client, withChatId))

fun createBotContext(
    client: HttpClient = TelegramApi.defaultClientInstance,
    withChatId: ChatId? = null,
) = object : BotContext<UnknownEventHandler> {
    override val client = client
    override fun getChatId(): ChatId? = withChatId
    override suspend fun <R> withBot(bot: Bot, block: suspend BotContext<UnknownEventHandler>.() -> R): R {
        return UnknownEventHandler(Update(-1), bot.client).block()
    }
}

@DslMarker
annotation class BotContextDsl

internal fun noimpl(): Nothing = throw NotImplementedError()

/**
 * Returns sticker name with base [name] appended with `_by_<bot_username>`
 *
 * @param bot current bot for getting [Bot.me]. If it is null then [getMe][API.getMe] was called
 * */
// TODO can move me to the context?
suspend fun <T : EventHandler> BotContext<T>.stickerSetName(name: String, bot: Bot? = null) =
    "${name}_by_${bot?.me?.username ?: getMe().username}"