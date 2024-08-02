package ru.raysmith.tgbot.core.handler.base

import io.ktor.client.*
import ru.raysmith.tgbot.core.Bot
import ru.raysmith.tgbot.core.BotConfig
import ru.raysmith.tgbot.core.BotContext
import ru.raysmith.tgbot.core.handler.BaseEventHandler
import ru.raysmith.tgbot.core.handler.HandlerDsl
import ru.raysmith.tgbot.model.network.message.reaction.MessageReactionCountUpdated

@HandlerDsl
open class MessageReactionCountHandler(
    val messageReactionCount: MessageReactionCountUpdated,
    final override val bot: Bot,
    private val handler: suspend MessageReactionCountHandler.() -> Unit = {}
) : BaseEventHandler(), BotContext<MessageReactionCountHandler> {
    override val client: HttpClient = bot.client
    override val botConfig: BotConfig = bot.botConfig

    override var messageId: Int? = messageReactionCount.messageId
    override var inlineMessageId: String? = null

    override fun getChatId() = messageReactionCount.chat.id
    override fun getChatIdOrThrow() = messageReactionCount.chat.id

    override suspend fun handle() {
        handler()
        handled = true
        handleLocalFeatures(handled)
    }

    override suspend fun <R> withBot(bot: Bot, block: suspend BotContext<MessageReactionCountHandler>.() -> R): R {
        return MessageReactionCountHandler(messageReactionCount, bot, handler).block()
    }
}