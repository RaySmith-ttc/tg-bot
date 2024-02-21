package ru.raysmith.tgbot.utils

import io.ktor.client.*
import ru.raysmith.tgbot.core.Bot
import ru.raysmith.tgbot.core.BotContext
import ru.raysmith.tgbot.core.BotContextDsl
import ru.raysmith.tgbot.core.handler.BaseEventHandler
import ru.raysmith.tgbot.core.handler.base.UnknownEventHandler
import ru.raysmith.tgbot.model.bot.ChatId
import ru.raysmith.tgbot.model.network.updates.Update
import ru.raysmith.tgbot.network.TelegramApi

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
) = botContext(bot.client, withChatId, update, block)

/**
 * Creates a bot context and executes a [block] within
 *
 * @param token Telegram bot token
 * @param withChatId Unique identifier for the target chat or username of the target channel
 * (in the format `@channelusername`). Overrides chatId from [update] if not null.
 * @param update update with which the context should be associated. If null, the update will be empty with an id of -1.
 * */
@BotContextDsl
suspend fun <T> botContext(
    token: String,
    withChatId: ChatId? = null,
    update: Update? = null,
    block: suspend context(BotContext<UnknownEventHandler>) () -> T
) = botContext(TelegramApi.defaultClient(token), withChatId, update, block)

/**
 * Creates a bot context and executes a [block] within
 *
 * @param withChatId Unique identifier for the target chat or username of the target channel
 * (in the format `@channelusername`). Overrides chatId from [update] if not null.
 * @param update update with which the context should be associated. If null, the update will be empty with an id of -1.
 * @param client
 * */
@BotContextDsl
suspend fun <T> botContext(
    client: HttpClient,
    withChatId: ChatId? = null,
    update: Update? = null,
    block: suspend context(BotContext<UnknownEventHandler>) () -> T
) = block(createBotContext(withChatId, update, client))

internal fun createBotContext(
    withChatId: ChatId? = null,
    update: Update? = null,
    client: HttpClient = TelegramApi.defaultClientInstance,
) = object : BotContext<UnknownEventHandler> {
    override val client = client
    override var messageId: Int? = update?.message?.messageId
    override var inlineMessageId: String? = update?.callbackQuery?.inlineMessageId

    override fun getChatId(): ChatId? = withChatId ?: update?.findChatId()
    override suspend fun <R> withBot(bot: Bot, block: suspend BotContext<UnknownEventHandler>.() -> R): R {
        return UnknownEventHandler(update ?: Update(-1), bot.client).block()
    }
}

internal fun createEventHandler(
    withChatId: ChatId? = null,
    withMessageId: Int? = null,
    withInlineMessageId: String? = null,
    client: HttpClient = TelegramApi.defaultClientInstance,
) = object : BaseEventHandler() {
    override suspend fun handle() {}
    override fun getChatId(): ChatId? = withChatId
    override var messageId: Int? = withMessageId
    override var inlineMessageId: String? = withInlineMessageId
    override val client: HttpClient = client
}

internal fun noimpl(): Nothing = throw NotImplementedError()