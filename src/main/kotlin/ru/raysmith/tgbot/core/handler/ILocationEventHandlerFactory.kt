package ru.raysmith.tgbot.core.handler

import ru.raysmith.tgbot.core.Bot
import ru.raysmith.tgbot.core.BotHolder
import ru.raysmith.tgbot.core.handler.base.UnknownEventHandler
import ru.raysmith.tgbot.core.handler.location.*
import ru.raysmith.tgbot.model.network.message.MessageType
import ru.raysmith.tgbot.model.network.updates.Update
import ru.raysmith.tgbot.model.network.updates.UpdateType
import ru.raysmith.tgbot.utils.locations.LocationConfig
import ru.raysmith.tgbot.utils.locations.LocationsWrapper

interface ILocationEventHandlerFactory<T : LocationConfig> : EventHandlerFactory, BotHolder {
    val locationsWrapper: LocationsWrapper<T>

    @HandlerDsl
    fun handleUnknown(handler: suspend UnknownEventHandler.() -> Unit)

    @HandlerDsl
    fun handleMessage(handler: suspend (context(T) LocationMessageHandler<T>.() -> Unit))

    @HandlerDsl
    fun handleCommand(handler:suspend (context(T) LocationCommandHandler<T>.() -> Unit))

    @HandlerDsl
    fun handleEditedMessage(handler: suspend (context(T) LocationEditedMessageHandler<T>.() -> Unit))

    @HandlerDsl
    fun handleChannelPost(handler: suspend (context(T) LocationChannelPostHandler<T>.() -> Unit))

    @HandlerDsl
    fun handleEditedChannelPost(handler: suspend (context(T) LocationEditedChannelPostHandler<T>.() -> Unit))

    @HandlerDsl
    fun handleInlineQuery(handler: suspend (context(T) LocationInlineQueryHandler<T>.() -> Unit))

    @HandlerDsl
    fun handleChosenInlineQuery(handler: suspend (context(T) LocationChosenInlineQueryHandler<T>.() -> Unit))

    @HandlerDsl
    fun handleCallbackQuery(
        alwaysAnswer: Boolean = bot.botConfig.alwaysAnswerCallback,
        handler: (suspend context(T) LocationCallbackQueryHandler<T>.() -> Unit)?
    )

    @HandlerDsl
    fun handleShippingQuery(handler: suspend (context(T) LocationShippingQueryHandler<T>.() -> Unit))

    @HandlerDsl
    fun handlePreCheckoutQuery(handler: suspend (context(T) LocationPreCheckoutQueryHandler<T>.() -> Unit))

    @HandlerDsl
    fun handlePoll(handler: suspend (context(T) LocationPollHandler<T>.() -> Unit))

    @HandlerDsl
    fun handlePollAnswer(handler: suspend (context(T) LocationPollAnswerHandler<T>.() -> Unit))

    @HandlerDsl
    fun handleMyChatMember(handler: suspend (context(T) LocationChatMemberHandler<T>.() -> Unit))

    @HandlerDsl
    fun handleChatMember(handler: suspend (context(T) LocationChatMemberHandler<T>.() -> Unit))

    @HandlerDsl
    fun handleChatJoinRequest(handler: suspend (context(T) LocationChatJoinRequestHandler<T>.() -> Unit))
}

@HandlerDsl
class LocationEventHandlerFactory<T : LocationConfig>(
    override val locationsWrapper: LocationsWrapper<T>,
) : ILocationEventHandlerFactory<T> {
    override val bot: Bot = locationsWrapper.bot

    override val allowedUpdates = mutableSetOf<UpdateType>()

    private val messageHandler: MutableList<LocationMessageHandlerData<T>> = mutableListOf()
    private val commandHandler: MutableList<LocationCommandHandlerData<T>> = mutableListOf()
    private val editedMessageHandler: MutableList<LocationEditMessageHandlerData<T>> = mutableListOf()
    private val channelPostHandler: MutableList<LocationChannelPostHandlerData<T>> = mutableListOf()
    private val editedChannelPostHandler: MutableList<LocationEditedChannelPostHandlerData<T>> = mutableListOf()
    private val inlineQueryHandler: MutableList<LocationInlineQueryHandlerData<T>> = mutableListOf()
    private val chosenInlineQueryHandler: MutableList<LocationChosenInlineQueryHandlerData<T>> = mutableListOf()
    private val callbackQueryHandler: MutableList<LocationCallbackQueryHandlerData<T>> = mutableListOf()
    private val shippingQueryHandler: MutableList<LocationShippingQueryHandlerData<T>> = mutableListOf()
    private val preCheckoutQueryHandler: MutableList<LocationPreCheckoutQueryHandlerData<T>> = mutableListOf()
    private val pollHandler: MutableList<LocationPollHandlerData<T>> = mutableListOf()
    private val pollAnswerHandler: MutableList<LocationPollAnswerHandlerData<T>> = mutableListOf()
    private val myChatMemberHandler: MutableList<LocationChatMemberHandlerData<T>> = mutableListOf()
    private val chatMemberHandler: MutableList<LocationChatMemberHandlerData<T>> = mutableListOf()
    private val chatJoinRequestHandler: MutableList<LocationChatJoinRequestHandlerData<T>> = mutableListOf()

    private var unknownHandler: suspend UnknownEventHandler.() -> Unit = { }

    override fun clear() {
        allowedUpdates.clear()
        messageHandler.clear()
        commandHandler.clear()
        editedMessageHandler.clear()
        channelPostHandler.clear()
        editedChannelPostHandler.clear()
        inlineQueryHandler.clear()
        chosenInlineQueryHandler.clear()
        callbackQueryHandler.clear()
        shippingQueryHandler.clear()
        preCheckoutQueryHandler.clear()
        pollHandler.clear()
        pollAnswerHandler.clear()
        myChatMemberHandler.clear()
        chatMemberHandler.clear()
        chatJoinRequestHandler.clear()
        unknownHandler = { }
    }

    override fun getHandler(update: Update): EventHandler = when(update.type) {
        UpdateType.MESSAGE -> when(update.message!!.type) {
            MessageType.COMMAND -> LocationCommandHandler(update, bot, commandHandler, locationsWrapper)
            MessageType.TEXT -> LocationMessageHandler(update, bot, messageHandler, locationsWrapper)
        }

        UpdateType.EDITED_MESSAGE -> LocationEditedMessageHandler(
            update, bot, editedMessageHandler, locationsWrapper
        )

        UpdateType.CHANNEL_POST -> LocationChannelPostHandler(
            update, bot, channelPostHandler, locationsWrapper
        )

        UpdateType.EDITED_CHANNEL_POST -> LocationEditedChannelPostHandler(
            update, bot, editedChannelPostHandler, locationsWrapper
        )

        UpdateType.INLINE_QUERY -> LocationInlineQueryHandler(
            update, bot, inlineQueryHandler, locationsWrapper
        )

        UpdateType.CHOSEN_INLINE_RESULT -> LocationChosenInlineQueryHandler(
            update, bot, chosenInlineQueryHandler, locationsWrapper
        )

        UpdateType.CALLBACK_QUERY -> LocationCallbackQueryHandler(
            update, bot, callbackQueryHandler, locationsWrapper
        )

        UpdateType.SHIPPING_QUERY -> LocationShippingQueryHandler(
            update, bot, shippingQueryHandler, locationsWrapper
        )

        UpdateType.PRE_CHECKOUT_QUERY -> LocationPreCheckoutQueryHandler(
            update, bot, preCheckoutQueryHandler, locationsWrapper
        )

        UpdateType.POLL -> LocationPollHandler(
            update, bot, pollHandler, locationsWrapper
        )

        UpdateType.POLL_ANSWER -> LocationPollAnswerHandler(
            update, bot, pollAnswerHandler, locationsWrapper
        )

        UpdateType.MY_CHAT_MEMBER -> LocationChatMemberHandler(
            update, bot, myChatMemberHandler, locationsWrapper
        )

        UpdateType.CHAT_MEMBER -> LocationChatMemberHandler(
            update, bot, chatMemberHandler, locationsWrapper
        )

        UpdateType.CHAT_JOIN_REQUEST -> LocationChatJoinRequestHandler(
            update, bot, chatJoinRequestHandler, locationsWrapper
        )

        null -> UnknownEventHandler(update, bot, unknownHandler)
    }

    @HandlerDsl
    override fun handleUnknown(handler: suspend UnknownEventHandler.() -> Unit) {
        unknownHandler = handler
    }


    @HandlerDsl
    override fun handleMessage(handler: suspend (context(T) LocationMessageHandler<T>.() -> Unit)) {
        allowedUpdates.add(UpdateType.MESSAGE)
        messageHandler.add(LocationMessageHandlerData(handler))
    }

    @HandlerDsl
    override fun handleCommand(handler:suspend (context(T) LocationCommandHandler<T>.() -> Unit)) {
        allowedUpdates.add(UpdateType.MESSAGE)
        commandHandler.add(LocationCommandHandlerData(handler))
    }

    @HandlerDsl
    override fun handleEditedMessage(handler: suspend (context(T) LocationEditedMessageHandler<T>.() -> Unit)) {
        allowedUpdates.add(UpdateType.EDITED_MESSAGE)
        editedMessageHandler.add(LocationEditMessageHandlerData(handler))
    }

    @HandlerDsl
    override fun handleChannelPost(handler: suspend (context(T) LocationChannelPostHandler<T>.() -> Unit)) {
        allowedUpdates.add(UpdateType.CHANNEL_POST)
        channelPostHandler.add(LocationChannelPostHandlerData(handler))
    }

    @HandlerDsl
    override fun handleEditedChannelPost(handler: suspend (context(T) LocationEditedChannelPostHandler<T>.() -> Unit)) {
        allowedUpdates.add(UpdateType.EDITED_CHANNEL_POST)
        editedChannelPostHandler.add(LocationEditedChannelPostHandlerData(handler))
    }

    @HandlerDsl
    override fun handleInlineQuery(handler: suspend (context(T) LocationInlineQueryHandler<T>.() -> Unit)) {
        allowedUpdates.add(UpdateType.INLINE_QUERY)
        inlineQueryHandler.add(LocationInlineQueryHandlerData(handler))
    }

    @HandlerDsl
    override fun handleChosenInlineQuery(handler: suspend (context(T) LocationChosenInlineQueryHandler<T>.() -> Unit)) {
        allowedUpdates.add(UpdateType.CHOSEN_INLINE_RESULT)
        chosenInlineQueryHandler.add(LocationChosenInlineQueryHandlerData(handler))
    }

    @HandlerDsl
    override fun handleCallbackQuery(
        alwaysAnswer: Boolean,
        handler: (suspend context(T) LocationCallbackQueryHandler<T>.() -> Unit)?
    ) {
        allowedUpdates.add(UpdateType.CALLBACK_QUERY)
        callbackQueryHandler.add(LocationCallbackQueryHandlerData(handler, alwaysAnswer))
    }

    @HandlerDsl
    override fun handleShippingQuery(handler: suspend (context(T) LocationShippingQueryHandler<T>.() -> Unit)) {
        allowedUpdates.add(UpdateType.SHIPPING_QUERY)
        shippingQueryHandler.add(LocationShippingQueryHandlerData(handler))
    }

    @HandlerDsl
    override fun handlePreCheckoutQuery(handler: suspend (context(T) LocationPreCheckoutQueryHandler<T>.() -> Unit)) {
        allowedUpdates.add(UpdateType.PRE_CHECKOUT_QUERY)
        preCheckoutQueryHandler.add(LocationPreCheckoutQueryHandlerData(handler))
    }

    @HandlerDsl
    override fun handlePoll(handler: suspend (context(T) LocationPollHandler<T>.() -> Unit)) {
        allowedUpdates.add(UpdateType.POLL)
        pollHandler.add(LocationPollHandlerData(handler))
    }

    @HandlerDsl
    override fun handlePollAnswer(handler: suspend (context(T) LocationPollAnswerHandler<T>.() -> Unit)) {
        allowedUpdates.add(UpdateType.POLL_ANSWER)
        pollAnswerHandler.add(LocationPollAnswerHandlerData(handler))
    }

    @HandlerDsl
    override fun handleMyChatMember(handler: suspend (context(T) LocationChatMemberHandler<T>.() -> Unit)) {
        allowedUpdates.add(UpdateType.MY_CHAT_MEMBER)
        myChatMemberHandler.add(LocationChatMemberHandlerData(handler))
    }

    @HandlerDsl
    override fun handleChatMember(handler: suspend (context(T) LocationChatMemberHandler<T>.() -> Unit)) {
        allowedUpdates.add(UpdateType.CHAT_MEMBER)
        chatMemberHandler.add(LocationChatMemberHandlerData(handler))
    }

    @HandlerDsl
    override fun handleChatJoinRequest(handler: suspend (context(T) LocationChatJoinRequestHandler<T>.() -> Unit)) {
        allowedUpdates.add(UpdateType.CHAT_JOIN_REQUEST)
        chatJoinRequestHandler.add(LocationChatJoinRequestHandlerData(handler))
    }
}