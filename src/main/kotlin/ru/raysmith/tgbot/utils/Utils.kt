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

fun botContext(bot: Bot, withChatId: ChatId? = null) = createBotContext(withChatId, bot.client)
fun botContext(token: String, withChatId: ChatId? = null) =
    createBotContext(withChatId, TelegramApi.defaultClient(token))

// TODO [docs] withMessageId and withInlineMessageId

/**
 * Creates a bot context and executes a [block] that can call API requests
 *
 * @param bot Telegram service with a bot token
 * @param withChatId Unique identifier for the target chat or username of the target channel (in the format `@channelusername`)
 * */
@BotContextDsl
suspend inline fun <T> botContext(
    bot: Bot,
    withChatId: ChatId? = null,
    withMessageId: Int? = null,
    withInlineMessageId: String? = null,
    crossinline block: suspend context(BotContext<UnknownEventHandler>) () -> T
) = botContext(withChatId, withMessageId, withInlineMessageId, bot.client, block)

/**
 * Creates a bot context and executes a [block] that can call API requests
 *
 * @param token Telegram bot token
 * @param withChatId Unique identifier for the target chat or username of the target channel (in the format `@channelusername`)
 * */
@BotContextDsl
suspend inline fun <T> botContext(
    token: String,
    withChatId: ChatId? = null,
    withMessageId: Int? = null,
    withInlineMessageId: String? = null,
    crossinline block: suspend context(BotContext<UnknownEventHandler>) () -> T
) =
    botContext(withChatId, withMessageId, withInlineMessageId, TelegramApi.defaultClient(token), block)

/**
 * Creates a bot context and executes a [block] that can call API requests
 *
 * @param withChatId Unique identifier for the target chat or username of the target channel (in the format `@channelusername`)
 * */
@BotContextDsl
suspend inline fun <T> botContext(
    withChatId: ChatId? = null,
    withMessageId: Int? = null,
    withInlineMessageId: String? = null,
    client: HttpClient = TelegramApi.defaultClientInstance,
    crossinline block: suspend context(BotContext<UnknownEventHandler>) () -> T
) = block(createBotContext(withChatId, client))

fun createBotContext(
    withChatId: ChatId? = null,
    client: HttpClient = TelegramApi.defaultClientInstance,
) = object : BotContext<UnknownEventHandler> {
    override val client = client
    override fun getChatId(): ChatId? = withChatId
    override suspend fun <R> withBot(bot: Bot, block: suspend BotContext<UnknownEventHandler>.() -> R): R {
        return UnknownEventHandler(Update(-1), bot.client).block()
    }
}

fun createEventHandler(
    withChatId: ChatId? = null,
    withMessageId: Int? = null,
    withInlineMessageId: String? = null,
    client: HttpClient = TelegramApi.defaultClientInstance,
) = object : EventHandler {
    override suspend fun handle() {}
    override fun getChatId(): ChatId? = withChatId
    override var messageId: Int? = withMessageId
    override var inlineMessageId: String? = withInlineMessageId
    override val client: HttpClient = client
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