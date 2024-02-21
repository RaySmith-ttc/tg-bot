package ru.raysmith.tgbot.core.handler

import io.ktor.client.*
import ru.raysmith.tgbot.core.Bot
import ru.raysmith.tgbot.core.handler.base.*
import ru.raysmith.tgbot.model.bot.BotCommand
import ru.raysmith.tgbot.model.network.message.MessageType
import ru.raysmith.tgbot.model.network.updates.Update
import ru.raysmith.tgbot.model.network.updates.UpdateType
import ru.raysmith.tgbot.utils.datepicker.BotFeature

@DslMarker
annotation class HandlerDsl

@HandlerDsl
open class BaseEventHandlerFactory : EventHandlerFactory {

    override val allowedUpdates = mutableSetOf<UpdateType>()

    private var messageHandler: (suspend (MessageHandler.() -> Unit))? = null
    private var commandHandler: (suspend (CommandHandler.() -> Unit))? = null
    private var editedMessageHandler: (suspend (EditedMessageHandler.() -> Unit))? = null
    private var channelPostHandler: (suspend (ChannelPostHandler.() -> Unit))? = null
    private var editedChannelPostHandler: (suspend (EditedChannelPostHandler.() -> Unit))? = null
    private var inlineQueryHandler: (suspend (InlineQueryHandler.() -> Unit))? = null
    private var chosenInlineQueryHandler: (suspend (ChosenInlineQueryHandler.() -> Unit))? = null
    private val callbackQueryHandler: MutableMap<String, CallbackQueryHandlerData> = mutableMapOf()
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

    override fun getHandler(update: Update, client: HttpClient): EventHandler {
        fun unknown() = UnknownEventHandler(update, client, unknownHandler)

        return when(update.type) {
            UpdateType.MESSAGE -> when(update.message!!.type) {
                MessageType.COMMAND -> CommandHandler(
                    BotCommand(update.message.text!!), update.message, client, commandHandler ?: return unknown()
                )
                MessageType.TEXT -> MessageHandler(
                    update.message, client, messageHandler ?: return unknown()
                )
            }

            UpdateType.EDITED_MESSAGE -> EditedMessageHandler(
                update.editedMessage!!, client, editedMessageHandler ?: return unknown()
            )

            UpdateType.CHANNEL_POST -> ChannelPostHandler(
                update.channelPost!!, client, channelPostHandler ?: return unknown()
            )

            UpdateType.EDITED_CHANNEL_POST -> EditedChannelPostHandler(
                update.editedChannelPost!!, client, editedChannelPostHandler ?: return unknown()
            )

            UpdateType.INLINE_QUERY -> InlineQueryHandler(
                update.inlineQuery!!, client, inlineQueryHandler ?: return unknown()
            )

            UpdateType.CHOSEN_INLINE_RESULT -> ChosenInlineQueryHandler(
                update.chosenInlineResult!!, client, chosenInlineQueryHandler ?: return unknown()
            )

            UpdateType.CALLBACK_QUERY -> CallbackQueryHandler(
                update.callbackQuery!!, callbackQueryHandler, client
            )

            UpdateType.SHIPPING_QUERY -> ShippingQueryHandler(
                update.shippingQuery!!, client, shippingQueryHandler ?: return unknown()
            )

            UpdateType.PRE_CHECKOUT_QUERY -> PreCheckoutQueryHandler(
                update.preCheckoutQuery!!, client, preCheckoutQueryHandler ?: return unknown()
            )

            UpdateType.POLL -> PollHandler(
                update.poll!!, client, pollHandler ?: return unknown()
            )

            UpdateType.POLL_ANSWER -> PollAnswerHandler(
                update.pollAnswer!!, client, pollAnswerHandler ?: return unknown()
            )


            UpdateType.MY_CHAT_MEMBER -> ChatMemberHandler(
                update.myChatMember!!, client, myChatMemberHandler ?: return unknown()
            )

            UpdateType.CHAT_MEMBER -> ChatMemberHandler(
                update.chatMember!!, client, chatMemberHandler ?: return unknown()
            )

            UpdateType.CHAT_JOIN_REQUEST -> ChatJoinRequestHandler(
                update.chatJoinRequest!!, client, chatJoinRequestHandler ?: return unknown()
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
        alwaysAnswer: Boolean = Bot.config.alwaysAnswerCallback,
        features: List<BotFeature> = Bot.config.defaultCallbackQueryHandlerFeatures,
        handlerId: String = CallbackQueryHandler.HANDLER_ID,
        handler: (suspend (CallbackQueryHandler.() -> Unit))?
    ) {
        if (callbackQueryHandler.containsKey(handlerId)) {
            throw IllegalArgumentException("Callback handler with id '$handlerId' already registered")
        }
        allowedUpdates.add(UpdateType.CALLBACK_QUERY)
        Bot.logger.debug("Register callbackQueryHandler '${handlerId}'")
        callbackQueryHandler[handlerId] = CallbackQueryHandlerData(handler, features.toMutableList(), alwaysAnswer)
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