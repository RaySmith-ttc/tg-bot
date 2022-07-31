package ru.raysmith.tgbot.utils

import ru.raysmith.tgbot.core.Bot
import ru.raysmith.tgbot.core.BotContext
import ru.raysmith.tgbot.core.handler.UnknownEventHandler
import ru.raysmith.tgbot.model.bot.ChatId
import ru.raysmith.tgbot.model.network.updates.Update
import ru.raysmith.tgbot.network.TelegramApi
import ru.raysmith.tgbot.network.TelegramFileService
import ru.raysmith.tgbot.network.TelegramService

internal fun errorBody(): Nothing = throw NullPointerException("The method did not return a body")

/**
 * Creates a bot context and executes a [block] that can call API requests
 *
 * @param bot Telegram service with a bot token
 * @param withChatId Unique identifier for the target chat or username of the target channel (in the format `@channelusername`)
 * */
inline fun <T> botContext(bot: Bot, withChatId: ChatId? = null, block: BotContext<UnknownEventHandler>.() -> T) =
    botContext(bot.service, bot.fileService, withChatId, block)

/**
 * Creates a bot context and executes a [block] that can call API requests
 *
 * @param token Telegram bot token
 * @param withChatId Unique identifier for the target chat or username of the target channel (in the format `@channelusername`)
 * */
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
) = object : BotContext<UnknownEventHandler> {
    override val service: TelegramService = service
    override val fileService: TelegramFileService = fileService

    override fun getChatId(): ChatId? = withChatId
    override fun withBot(bot: Bot, block: BotContext<UnknownEventHandler>.() -> Any) {
        UnknownEventHandler(Update(-1), service, fileService).apply {
            block()
        }
    }
}.let(block)

@DslMarker
annotation class BotContextDsl