package ru.raysmith.tgbot.utils

import io.ktor.client.*
import ru.raysmith.tgbot.core.*
import ru.raysmith.tgbot.core.handler.BaseEventHandler
import ru.raysmith.tgbot.core.handler.base.UnknownEventHandler
import ru.raysmith.tgbot.model.bot.ChatId
import ru.raysmith.tgbot.model.network.updates.Update

/**
 * Creates a bot context and executes a [block] within
 *
 * @param bot bot instance
 * @param withChatId Unique identifier for the target chat or username of the target channel
 * (in the format `@channelusername`). Overrides chatId from [update] if not null.
 * @param update update with which the context should be associated. If null, the update will be empty with an id of -1.
 * */
@BotContextDsl
inline fun <T> botContext(
    bot: Bot,
    withChatId: ChatId? = null,
    update: Update? = null,
    block: context(BotContext<UnknownEventHandler>) () -> T
): BotContext<UnknownEventHandler> = object : BotContext<UnknownEventHandler> {
    override val bot = bot
    override val client: HttpClient = bot.client
    override val botConfig: BotConfig = bot.botConfig
    override var messageId: Int? = update?.message?.messageId
    override var inlineMessageId: String? = update?.callbackQuery?.inlineMessageId
    @Deprecated("Always null because the business connection id cannot be obtain from the event")
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
): BaseEventHandler = object : BaseEventHandler() {
    override suspend fun handle() {}
    override fun getChatId(): ChatId? = withChatId
    override var messageId: Int? = withMessageId
    override var inlineMessageId: String? = withInlineMessageId
    override val bot: Bot = bot
    override val client: HttpClient = bot.client
    override val botConfig: BotConfig = bot.botConfig
}

internal fun noimpl(): Nothing = throw NotImplementedError()