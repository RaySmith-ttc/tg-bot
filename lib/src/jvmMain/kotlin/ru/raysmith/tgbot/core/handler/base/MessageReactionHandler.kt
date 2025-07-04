package ru.raysmith.tgbot.core.handler.base

import io.ktor.client.*
import ru.raysmith.tgbot.core.Bot
import ru.raysmith.tgbot.core.BotConfig
import ru.raysmith.tgbot.core.BotContext
import ru.raysmith.tgbot.core.handler.BaseEventHandler
import ru.raysmith.tgbot.model.network.message.reaction.MessageReactionUpdated

open class MessageReactionHandler(
    val messageReaction: MessageReactionUpdated,
    final override val bot: Bot,
    private val handler: suspend MessageReactionHandler.() -> Unit = {}
) : BaseEventHandler(), BotContext<MessageReactionHandler> {
    override val client: HttpClient = bot.client
    override val botConfig: BotConfig = bot.botConfig

    override var messageId: Int? = messageReaction.messageId
    override var inlineMessageId: String? = null

    override fun getChatId() = messageReaction.chat.id
    override fun getChatIdOrThrow() = messageReaction.chat.id

    override suspend fun handle() {
        handler()
        handled = true
        handleLocalFeatures(handled)
    }

    override suspend fun <R> withBot(bot: Bot, block: suspend BotContext<MessageReactionHandler>.() -> R): R {
        return MessageReactionHandler(messageReaction, bot, handler).block()
    }
}