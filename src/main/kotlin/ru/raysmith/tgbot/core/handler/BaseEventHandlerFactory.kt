package ru.raysmith.tgbot.core.handler

import ru.raysmith.tgbot.core.Bot
import ru.raysmith.tgbot.core.BotHolder
import ru.raysmith.tgbot.core.handler.base.*
import ru.raysmith.tgbot.model.bot.BotCommand
import ru.raysmith.tgbot.model.network.message.MessageType
import ru.raysmith.tgbot.model.network.updates.Update
import ru.raysmith.tgbot.model.network.updates.UpdateType

@DslMarker
annotation class HandlerDsl

@HandlerDsl
open class BaseEventHandlerFactory(override val bot: Bot) : EventHandlerFactory, BotHolder {

    override val allowedUpdates = mutableSetOf<UpdateType>()

    private var messageHandler: (suspend (MessageHandler.() -> Unit))? = null
    private var commandHandler: (suspend (CommandHandler.() -> Unit))? = null
    private var editedMessageHandler: (suspend (EditedMessageHandler.() -> Unit))? = null
    private var channelPostHandler: (suspend (ChannelPostHandler.() -> Unit))? = null
    private var editedChannelPostHandler: (suspend (EditedChannelPostHandler.() -> Unit))? = null
    private var inlineQueryHandler: (suspend (InlineQueryHandler.() -> Unit))? = null
    private var chosenInlineQueryHandler: (suspend (ChosenInlineQueryHandler.() -> Unit))? = null
    private val callbackQueryHandler: MutableList<CallbackQueryHandlerData> = mutableListOf()
    private var shippingQueryHandler: (suspend (ShippingQueryHandler.() -> Unit))? = null
    private var preCheckoutQueryHandler: (suspend (PreCheckoutQueryHandler.() -> Unit))? = null
    private var pollHandler: (suspend (PollHandler.() -> Unit))? = null
    private var pollAnswerHandler: (suspend (PollAnswerHandler.() -> Unit))? = null
    private var myChatMemberHandler: (suspend (ChatMemberHandler.() -> Unit))? = null
    private var chatMemberHandler: (suspend (ChatMemberHandler.() -> Unit))? = null
    private var chatJoinRequestHandler: (suspend (ChatJoinRequestHandler.() -> Unit))? = null

    private var unknownHandler: suspend UnknownEventHandler.() -> Unit = { }

    override fun clear() {
        allowedUpdates.clear()
        messageHandler = null
        commandHandler = null
        editedMessageHandler = null
        channelPostHandler = null
        editedChannelPostHandler = null
        inlineQueryHandler = null
        chosenInlineQueryHandler = null
        callbackQueryHandler.clear()
        shippingQueryHandler = null
        preCheckoutQueryHandler = null
        pollHandler = null
        pollAnswerHandler = null
        myChatMemberHandler = null
        chatMemberHandler = null
        chatJoinRequestHandler = null
        unknownHandler = { }
    }

    override fun getHandler(update: Update): EventHandler {
        fun unknown() = UnknownEventHandler(update, bot, unknownHandler)

        return when(update.type) {
            UpdateType.MESSAGE -> when(update.message!!.type) {
                MessageType.COMMAND -> CommandHandler(
                    BotCommand(update.message.text!!), update.message, bot, commandHandler ?: return unknown()
                )
                MessageType.TEXT -> MessageHandler(
                    update.message, bot, messageHandler ?: return unknown()
                )
            }

            UpdateType.EDITED_MESSAGE -> EditedMessageHandler(
                update.editedMessage!!, bot, editedMessageHandler ?: return unknown()
            )

            UpdateType.CHANNEL_POST -> ChannelPostHandler(
                update.channelPost!!, bot, channelPostHandler ?: return unknown()
            )

            UpdateType.EDITED_CHANNEL_POST -> EditedChannelPostHandler(
                update.editedChannelPost!!, bot, editedChannelPostHandler ?: return unknown()
            )

            UpdateType.INLINE_QUERY -> InlineQueryHandler(
                update.inlineQuery!!, bot, inlineQueryHandler ?: return unknown()
            )

            UpdateType.CHOSEN_INLINE_RESULT -> ChosenInlineQueryHandler(
                update.chosenInlineResult!!, bot, chosenInlineQueryHandler ?: return unknown()
            )

            UpdateType.CALLBACK_QUERY -> CallbackQueryHandler(
                update.callbackQuery!!, callbackQueryHandler, bot
            )

            UpdateType.SHIPPING_QUERY -> ShippingQueryHandler(
                update.shippingQuery!!, bot, shippingQueryHandler ?: return unknown()
            )

            UpdateType.PRE_CHECKOUT_QUERY -> PreCheckoutQueryHandler(
                update.preCheckoutQuery!!, bot, preCheckoutQueryHandler ?: return unknown()
            )

            UpdateType.POLL -> PollHandler(
                update.poll!!, bot, pollHandler ?: return unknown()
            )

            UpdateType.POLL_ANSWER -> PollAnswerHandler(
                update.pollAnswer!!, bot, pollAnswerHandler ?: return unknown()
            )


            UpdateType.MY_CHAT_MEMBER -> ChatMemberHandler(
                update.myChatMember!!, bot, myChatMemberHandler ?: return unknown()
            )

            UpdateType.CHAT_MEMBER -> ChatMemberHandler(
                update.chatMember!!, bot, chatMemberHandler ?: return unknown()
            )

            UpdateType.CHAT_JOIN_REQUEST -> ChatJoinRequestHandler(
                update.chatJoinRequest!!, bot, chatJoinRequestHandler ?: return unknown()
            )

            null -> unknown()
        }
    }

    @HandlerDsl
    fun handleMessage(handler: suspend MessageHandler.() -> Unit) {
        allowedUpdates.add(UpdateType.MESSAGE)
        messageHandler = handler
    }

    @HandlerDsl
    fun handleCommand(handler: suspend CommandHandler.() -> Unit) {
        allowedUpdates.add(UpdateType.MESSAGE)
        commandHandler = handler
    }

    @HandlerDsl
    fun handleEditedMessage(handler: suspend EditedMessageHandler.() -> Unit) {
        allowedUpdates.add(UpdateType.EDITED_MESSAGE)
        editedMessageHandler = handler
    }

    @HandlerDsl
    fun handleChannelPost(handler: suspend ChannelPostHandler.() -> Unit) {
        allowedUpdates.add(UpdateType.CHANNEL_POST)
        channelPostHandler = handler
    }

    @HandlerDsl
    fun handleEditedChannelPost(handler: suspend EditedChannelPostHandler.() -> Unit) {
        allowedUpdates.add(UpdateType.EDITED_CHANNEL_POST)
        editedChannelPostHandler = handler
    }

    @HandlerDsl
    fun handleInlineQuery(handler: suspend InlineQueryHandler.() -> Unit) {
        allowedUpdates.add(UpdateType.INLINE_QUERY)
        inlineQueryHandler = handler
    }

    @HandlerDsl
    fun handleChosenInlineQuery(handler: suspend ChosenInlineQueryHandler.() -> Unit) {
        allowedUpdates.add(UpdateType.CHOSEN_INLINE_RESULT)
        chosenInlineQueryHandler = handler
    }
    
    @HandlerDsl
    fun handleCallbackQuery(
        alwaysAnswer: Boolean = bot.botConfig.alwaysAnswerCallback,
        handler: (suspend (CallbackQueryHandler.() -> Unit))?
    ) {
        allowedUpdates.add(UpdateType.CALLBACK_QUERY)
        callbackQueryHandler.add(CallbackQueryHandlerData(handler, alwaysAnswer))
    }

    @HandlerDsl
    fun handleShippingQuery(handler: suspend ShippingQueryHandler.() -> Unit) {
        allowedUpdates.add(UpdateType.SHIPPING_QUERY)
        shippingQueryHandler = handler
    }

    @HandlerDsl
    fun handlePreCheckoutQuery(handler: suspend PreCheckoutQueryHandler.() -> Unit) {
        allowedUpdates.add(UpdateType.PRE_CHECKOUT_QUERY)
        preCheckoutQueryHandler = handler
    }

    @HandlerDsl
    fun handlePoll(handler: suspend PollHandler.() -> Unit) {
        allowedUpdates.add(UpdateType.POLL)
        pollHandler = handler
    }

    @HandlerDsl
    fun handlePollAnswer(handler: suspend PollAnswerHandler.() -> Unit) {
        allowedUpdates.add(UpdateType.POLL_ANSWER)
        pollAnswerHandler = handler
    }

    @HandlerDsl
    fun handleMyChatMember(handler: suspend ChatMemberHandler.() -> Unit) {
        allowedUpdates.add(UpdateType.MY_CHAT_MEMBER)
        myChatMemberHandler = handler
    }

    @HandlerDsl
    fun handleChatMember(handler: suspend ChatMemberHandler.() -> Unit) {
        allowedUpdates.add(UpdateType.CHAT_MEMBER)
        chatMemberHandler = handler
    }

    @HandlerDsl
    fun handleChatJoinRequest(handler: suspend ChatJoinRequestHandler.() -> Unit) {
        allowedUpdates.add(UpdateType.CHAT_JOIN_REQUEST)
        chatJoinRequestHandler = handler
    }

    @HandlerDsl
    fun handleUnknown(handler: suspend UnknownEventHandler.() -> Unit) {
        unknownHandler = handler
    }
}