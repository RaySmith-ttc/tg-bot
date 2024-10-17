package ru.raysmith.tgbot.utils

import io.ktor.client.*
import ru.raysmith.tgbot.core.Bot
import ru.raysmith.tgbot.core.BotConfig
import ru.raysmith.tgbot.core.BotContext
import ru.raysmith.tgbot.core.BotContextDsl
import ru.raysmith.tgbot.core.handler.BaseEventHandler
import ru.raysmith.tgbot.core.handler.base.UnknownEventHandler
import ru.raysmith.tgbot.model.bot.ChatId
import ru.raysmith.tgbot.model.network.updates.Update
import java.net.URLDecoder
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec

/**
 * Creates a bot context and executes a [block] within
 *
 * @param bot bot instance
 * @param withChatId Unique identifier for the target chat or username of the target channel
 * (in the format `@channelusername`). Overrides chatId from [update] if not null.
 * @param update update with which the context should be associated. If null, the update will be empty with an id of -1.
 * */
@BotContextDsl
suspend fun <T> botContext(
    bot: Bot,
    withChatId: ChatId? = null,
    update: Update? = null,
    block: suspend context(BotContext<UnknownEventHandler>) () -> T
) = object : BotContext<UnknownEventHandler> {
    override val bot = bot
    override val client: HttpClient = bot.client
    override val botConfig: BotConfig = bot.botConfig
    override var messageId: Int? = update?.message?.messageId
    override var inlineMessageId: String? = update?.callbackQuery?.inlineMessageId
    @Deprecated("Always null the because business connection id cannot be obtain from the event")
    override var businessConnectionId: String? = null

    override fun getChatId(): ChatId? = withChatId ?: update?.findChatId()
    override suspend fun <R> withBot(bot: Bot, block: suspend BotContext<UnknownEventHandler>.() -> R): R {
        return UnknownEventHandler(update ?: Update(-1), bot).block()
    }
}.also { block(it) }


internal fun createEventHandler(
    bot: Bot,
    withChatId: ChatId? = null,
    withMessageId: Int? = null,
    withInlineMessageId: String? = null
) = object : BaseEventHandler() {
    override suspend fun handle() {}
    override fun getChatId(): ChatId? = withChatId
    override var messageId: Int? = withMessageId
    override var inlineMessageId: String? = withInlineMessageId
    override val bot: Bot = bot
    override val client: HttpClient = bot.client
    override val botConfig: BotConfig = bot.botConfig
}

internal fun noimpl(): Nothing = throw NotImplementedError()

// ---------------------------------------------------------------------------------------------------------------------

fun Bot.verifyWebAppInitData(initData: String): Boolean {
    val botToken = botConfig.token
    check(botToken != null) {
        "Token not found. Provide it with TG_BOT_TOKEN environment variable or token property"
    }

    val params = parseQueryString(initData)
    val receivedHash = params["hash"] ?: return false
    val dataCheckString = params
        .filterKeys { it != "hash" }
        .map { "${it.key}=${it.value}" }
        .sorted()
        .joinToString("\n")

    val secretKey = hmacSha256(botToken, "WebAppData".toByteArray())
    val computedHash = hmacSha256(dataCheckString, secretKey).joinToString("") { "%02x".format(it) }

    return computedHash == receivedHash
}

private fun hmacSha256(data: String, key: ByteArray): ByteArray {
    val mac = Mac.getInstance("HmacSHA256")
    val secretKeySpec = SecretKeySpec(key, "HmacSHA256")
    mac.init(secretKeySpec)
    return mac.doFinal(data.toByteArray(Charsets.UTF_8))
}

private fun parseQueryString(query: String): Map<String, String> {
    return query.split('&')
        .mapNotNull {
            val parts = it.split('=', limit = 2)
            if (parts.size == 2) {
                val key = URLDecoder.decode(parts[0], "UTF-8")
                val value = URLDecoder.decode(parts[1], "UTF-8")
                key to value
            } else {
                null
            }
        }
        .toMap()
}