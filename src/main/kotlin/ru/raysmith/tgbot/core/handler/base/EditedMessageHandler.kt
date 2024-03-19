package ru.raysmith.tgbot.core.handler.base

import ru.raysmith.tgbot.core.Bot
import ru.raysmith.tgbot.core.BotContext
import ru.raysmith.tgbot.core.handler.BaseEventHandler
import ru.raysmith.tgbot.core.handler.HandlerDsl
import ru.raysmith.tgbot.model.network.message.Message

@HandlerDsl
open class EditedMessageHandler(
    val message: Message,
    final override val bot: Bot,
    private val handler: suspend EditedMessageHandler.() -> Unit = { }
) : BaseEventHandler(), BotContext<EditedMessageHandler> {

    val editDate = message.editDate!!
    override fun getChatId() = message.chat.id
    override fun getChatIdOrThrow() = message.chat.id
    override var messageId: Int? = message.messageId
    override var inlineMessageId: String? = null

    override suspend fun handle() {
        handler()
        handled = true
        handleLocalFeatures(handled)
    }

    override suspend fun <R> withBot(bot: Bot, block: suspend BotContext<EditedMessageHandler>.() -> R): R {
        return EditedMessageHandler(message, bot, handler).block()
    }
}