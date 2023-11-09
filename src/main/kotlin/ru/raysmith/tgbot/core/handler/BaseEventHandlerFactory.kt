package ru.raysmith.tgbot.core.handler

import io.ktor.client.*
import ru.raysmith.tgbot.core.Bot
import ru.raysmith.tgbot.core.handler.base.*
import ru.raysmith.tgbot.model.bot.BotCommand
import ru.raysmith.tgbot.model.network.message.MessageType
import ru.raysmith.tgbot.model.network.updates.Update
import ru.raysmith.tgbot.model.network.updates.UpdateType
import ru.raysmith.tgbot.utils.datepicker.DatePicker

@DslMarker
annotation class HandlerDsl

// TODO add userShared, chatShared
@HandlerDsl
open class BaseEventHandlerFactory : EventHandlerFactory {

    override val allowedUpdates = mutableSetOf<UpdateType>()

    private var messageHandler: (MessageHandler.() -> Unit)? = null
    private var commandHandler: (CommandHandler.() -> Unit)? = null
    private var editedMessageHandler: (EditedMessageHandler.() -> Unit)? = null
    private var channelPostHandler: (ChannelPostHandler.() -> Unit)? = null
    private var editedChannelPostHandler: (EditedChannelPostHandler.() -> Unit)? = null
    private var inlineQueryHandler: (InlineQueryHandler.() -> Unit)? = null
    private var chosenInlineQueryHandler: (ChosenInlineQueryHandler.() -> Unit)? = null
    private val callbackQueryHandler: MutableMap<String, CallbackQueryHandlerData> = mutableMapOf()
    private var shippingQueryHandler: (ShippingQueryHandler.() -> Unit)? = null
    private var preCheckoutQueryHandler: (PreCheckoutQueryHandler.() -> Unit)? = null
    private var pollHandler: (PollHandler.() -> Unit)? = null
    private var pollAnswerHandler: (PollAnswerHandler.() -> Unit)? = null
    private var myChatMemberHandler: (ChatMemberHandler.() -> Unit)? = null
    private var chatMemberHandler: (ChatMemberHandler.() -> Unit)? = null
    private var chatJoinRequestHandler: (ChatJoinRequestHandler.() -> Unit)? = null

    private var unknownHandler: UnknownEventHandler.() -> Unit = { }

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
    fun handleMessage(handler: MessageHandler.() -> Unit) {
        allowedUpdates.add(UpdateType.MESSAGE)
        messageHandler = handler
    }

    @HandlerDsl
    fun handleCommand(handler: CommandHandler.() -> Unit) {
        allowedUpdates.add(UpdateType.MESSAGE)
        commandHandler = handler
    }

    @HandlerDsl
    fun handleEditedMessage(handler: EditedMessageHandler.() -> Unit) {
        allowedUpdates.add(UpdateType.EDITED_MESSAGE)
        editedMessageHandler = handler
    }

    @HandlerDsl
    fun handleChannelPost(handler: ChannelPostHandler.() -> Unit) {
        allowedUpdates.add(UpdateType.CHANNEL_POST)
        channelPostHandler = handler
    }

    @HandlerDsl
    fun handleEditedChannelPost(handler: EditedChannelPostHandler.() -> Unit) {
        allowedUpdates.add(UpdateType.EDITED_CHANNEL_POST)
        editedChannelPostHandler = handler
    }

    @HandlerDsl
    fun handleInlineQuery(handler: InlineQueryHandler.() -> Unit) {
        allowedUpdates.add(UpdateType.INLINE_QUERY)
        inlineQueryHandler = handler
    }

    @HandlerDsl
    fun handleChosenInlineQuery(handler: ChosenInlineQueryHandler.() -> Unit) {
        allowedUpdates.add(UpdateType.CHOSEN_INLINE_RESULT)
        chosenInlineQueryHandler = handler
    }
    
    @HandlerDsl
    fun handleCallbackQuery(
        alwaysAnswer: Boolean = Bot.config.alwaysAnswerCallback,
        handlerId: String = CallbackQueryHandler.HANDLER_ID,
        datePicker: DatePicker? = null,
        handler: (CallbackQueryHandler.() -> Unit)?
    ) {
        if (callbackQueryHandler.containsKey(handlerId)) {
            throw IllegalArgumentException("Callback handler with id '$handlerId' already registered")
        }
        allowedUpdates.add(UpdateType.CALLBACK_QUERY)
        Bot.logger.debug("Register callbackQueryHandler '${handlerId}'")
        callbackQueryHandler[handlerId] = CallbackQueryHandlerData(handler, datePicker, alwaysAnswer)
    }

    @HandlerDsl
    fun handleShippingQuery(handler: ShippingQueryHandler.() -> Unit) {
        allowedUpdates.add(UpdateType.SHIPPING_QUERY)
        shippingQueryHandler = handler
    }

    @HandlerDsl
    fun handlePreCheckoutQuery(handler: PreCheckoutQueryHandler.() -> Unit) {
        allowedUpdates.add(UpdateType.PRE_CHECKOUT_QUERY)
        preCheckoutQueryHandler = handler
    }

    @HandlerDsl
    fun handlePoll(handler: PollHandler.() -> Unit) {
        allowedUpdates.add(UpdateType.POLL)
        pollHandler = handler
    }

    @HandlerDsl
    fun handlePollAnswer(handler: PollAnswerHandler.() -> Unit) {
        allowedUpdates.add(UpdateType.POLL_ANSWER)
        pollAnswerHandler = handler
    }

    @HandlerDsl
    fun handleMyChatMember(handler: ChatMemberHandler.() -> Unit) {
        allowedUpdates.add(UpdateType.MY_CHAT_MEMBER)
        myChatMemberHandler = handler
    }

    @HandlerDsl
    fun handleChatMember(handler: ChatMemberHandler.() -> Unit) {
        allowedUpdates.add(UpdateType.CHAT_MEMBER)
        chatMemberHandler = handler
    }

    @HandlerDsl
    fun handleChatJoinRequest(handler: ChatJoinRequestHandler.() -> Unit) {
        allowedUpdates.add(UpdateType.CHAT_JOIN_REQUEST)
        chatJoinRequestHandler = handler
    }

    @HandlerDsl
    fun handleUnknown(handler: UnknownEventHandler.() -> Unit) {
        unknownHandler = handler
    }
}