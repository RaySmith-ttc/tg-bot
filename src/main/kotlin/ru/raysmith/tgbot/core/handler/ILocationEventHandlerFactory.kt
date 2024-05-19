package ru.raysmith.tgbot.core.handler

import ru.raysmith.tgbot.core.Bot
import ru.raysmith.tgbot.core.BotHolder
import ru.raysmith.tgbot.core.handler.base.CallbackQueryHandler
import ru.raysmith.tgbot.core.handler.base.UnknownEventHandler
import ru.raysmith.tgbot.core.handler.location.*
import ru.raysmith.tgbot.model.network.message.MessageType
import ru.raysmith.tgbot.model.network.updates.Update
import ru.raysmith.tgbot.model.network.updates.UpdateType
import ru.raysmith.tgbot.utils.locations.LocationConfig
import ru.raysmith.tgbot.utils.locations.LocationsWrapper

interface ILocationEventHandlerFactory<T : LocationConfig> : EventHandlerFactory, BotHolder {
    val locationsWrapper: LocationsWrapper<T>
    var defaultHandlerId: String  // TODO exposed to client

    @HandlerDsl
    fun handleUnknown(handler: suspend UnknownEventHandler.() -> Unit)

    @HandlerDsl
    fun handleMessage(handlerId: String = defaultHandlerId, handler: suspend (context(T) LocationMessageHandler<T>.() -> Unit))

    @HandlerDsl
    fun handleCommand(handlerId: String = defaultHandlerId, handler:suspend (context(T) LocationCommandHandler<T>.() -> Unit))

    @HandlerDsl
    fun handleEditedMessage(handlerId: String = defaultHandlerId, handler: suspend (context(T) LocationEditedMessageHandler<T>.() -> Unit))

    @HandlerDsl
    fun handleChannelPost(handlerId: String = defaultHandlerId, handler: suspend (context(T) LocationChannelPostHandler<T>.() -> Unit))

    @HandlerDsl
    fun handleEditedChannelPost(handlerId: String = defaultHandlerId, handler: suspend (context(T) LocationEditedChannelPostHandler<T>.() -> Unit))

    @HandlerDsl
    fun handleInlineQuery(handlerId: String = defaultHandlerId, handler: suspend (context(T) LocationInlineQueryHandler<T>.() -> Unit))

    @HandlerDsl
    fun handleChosenInlineQuery(handlerId: String = defaultHandlerId, handler: suspend (context(T) LocationChosenInlineQueryHandler<T>.() -> Unit))

    @HandlerDsl
    fun handleCallbackQuery(
        alwaysAnswer: Boolean = bot.botConfig.alwaysAnswerCallback,
        handlerId: String = defaultHandlerId,
        handler: (suspend context(T) LocationCallbackQueryHandler<T>.() -> Unit)?
    )

    @HandlerDsl
    fun handleShippingQuery(handlerId: String = defaultHandlerId, handler: suspend (context(T) LocationShippingQueryHandler<T>.() -> Unit))

    @HandlerDsl
    fun handlePreCheckoutQuery(handlerId: String = defaultHandlerId, handler: suspend (context(T) LocationPreCheckoutQueryHandler<T>.() -> Unit))

    @HandlerDsl
    fun handlePoll(handlerId: String = defaultHandlerId, handler: suspend (context(T) LocationPollHandler<T>.() -> Unit))

    @HandlerDsl
    fun handlePollAnswer(handlerId: String = defaultHandlerId, handler: suspend (context(T) LocationPollAnswerHandler<T>.() -> Unit))

    @HandlerDsl
    fun handleMyChatMember(handlerId: String = defaultHandlerId, handler: suspend (context(T) LocationChatMemberHandler<T>.() -> Unit))

    @HandlerDsl
    fun handleChatMember(handlerId: String = defaultHandlerId, handler: suspend (context(T) LocationChatMemberHandler<T>.() -> Unit))

    @HandlerDsl
    fun handleChatJoinRequest(handlerId: String = defaultHandlerId, handler: suspend (context(T) LocationChatJoinRequestHandler<T>.() -> Unit))
}

@HandlerDsl
class LocationEventHandlerFactory<T : LocationConfig>(
    override val locationsWrapper: LocationsWrapper<T>,
    override var defaultHandlerId: String = CallbackQueryHandler.HANDLER_ID
) : ILocationEventHandlerFactory<T> {
    override val bot: Bot = locationsWrapper.bot

    override val allowedUpdates = mutableSetOf<UpdateType>()

    private val messageHandler: MutableMap<String, LocationMessageHandlerData<T>> = mutableMapOf()
    private val commandHandler: MutableMap<String, LocationCommandHandlerData<T>> = mutableMapOf()
    private val editedMessageHandler: MutableMap<String, LocationEditMessageHandlerData<T>> = mutableMapOf()
    private val channelPostHandler: MutableMap<String, LocationChannelPostHandlerData<T>> = mutableMapOf()
    private val editedChannelPostHandler: MutableMap<String, LocationEditedChannelPostHandlerData<T>> = mutableMapOf()
    private val inlineQueryHandler: MutableMap<String, LocationInlineQueryHandlerData<T>> = mutableMapOf()
    private val chosenInlineQueryHandler: MutableMap<String, LocationChosenInlineQueryHandlerData<T>> = mutableMapOf()
    private val callbackQueryHandler: MutableMap<String, LocationCallbackQueryHandlerData<T>> = mutableMapOf()
    private val shippingQueryHandler: MutableMap<String, LocationShippingQueryHandlerData<T>> = mutableMapOf()
    private val preCheckoutQueryHandler: MutableMap<String, LocationPreCheckoutQueryHandlerData<T>> = mutableMapOf()
    private val pollHandler: MutableMap<String, LocationPollHandlerData<T>> = mutableMapOf()
    private val pollAnswerHandler: MutableMap<String, LocationPollAnswerHandlerData<T>> = mutableMapOf()
    private val myChatMemberHandler: MutableMap<String, LocationChatMemberHandlerData<T>> = mutableMapOf()
    private val chatMemberHandler: MutableMap<String, LocationChatMemberHandlerData<T>> = mutableMapOf()
    private val chatJoinRequestHandler: MutableMap<String, LocationChatJoinRequestHandlerData<T>> = mutableMapOf()

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
    override fun handleMessage(handlerId: String, handler: suspend (context(T) LocationMessageHandler<T>.() -> Unit)) {
        allowedUpdates.add(UpdateType.MESSAGE)
        messageHandler[handlerId] = LocationMessageHandlerData(handler)
    }

    @HandlerDsl
    override fun handleCommand(handlerId: String, handler:suspend (context(T) LocationCommandHandler<T>.() -> Unit)) {
        allowedUpdates.add(UpdateType.MESSAGE)
        commandHandler[handlerId] = LocationCommandHandlerData(handler)
    }

    @HandlerDsl
    override fun handleEditedMessage(handlerId: String, handler: suspend (context(T) LocationEditedMessageHandler<T>.() -> Unit)) {
        allowedUpdates.add(UpdateType.EDITED_MESSAGE)
        editedMessageHandler[handlerId] = LocationEditMessageHandlerData(handler)
    }

    @HandlerDsl
    override fun handleChannelPost(handlerId: String, handler: suspend (context(T) LocationChannelPostHandler<T>.() -> Unit)) {
        allowedUpdates.add(UpdateType.CHANNEL_POST)
        channelPostHandler[handlerId] = LocationChannelPostHandlerData(handler)
    }

    @HandlerDsl
    override fun handleEditedChannelPost(handlerId: String, handler: suspend (context(T) LocationEditedChannelPostHandler<T>.() -> Unit)) {
        allowedUpdates.add(UpdateType.EDITED_CHANNEL_POST)
        editedChannelPostHandler[handlerId] = LocationEditedChannelPostHandlerData(handler)
    }

    @HandlerDsl
    override fun handleInlineQuery(handlerId: String, handler: suspend (context(T) LocationInlineQueryHandler<T>.() -> Unit)) {
        allowedUpdates.add(UpdateType.INLINE_QUERY)
        inlineQueryHandler[handlerId] = LocationInlineQueryHandlerData(handler)
    }

    @HandlerDsl
    override fun handleChosenInlineQuery(handlerId: String, handler: suspend (context(T) LocationChosenInlineQueryHandler<T>.() -> Unit)) {
        allowedUpdates.add(UpdateType.CHOSEN_INLINE_RESULT)
        chosenInlineQueryHandler[handlerId] = LocationChosenInlineQueryHandlerData(handler)
    }

    @HandlerDsl
    override fun handleCallbackQuery(
        alwaysAnswer: Boolean,
        handlerId: String,
        handler: (suspend context(T) LocationCallbackQueryHandler<T>.() -> Unit)?
    ) {
        if (callbackQueryHandler.containsKey(handlerId)) return
        allowedUpdates.add(UpdateType.CALLBACK_QUERY)
        Bot.logger.debug("Register callbackQueryHandler '${handlerId}'")
        callbackQueryHandler[handlerId] = LocationCallbackQueryHandlerData(handler, alwaysAnswer)
    }

    internal suspend fun withHandlerId(handlerId: String, block: suspend LocationEventHandlerFactory<T>.() -> Unit) {
        val defaultHandlerIdBck = defaultHandlerId
        defaultHandlerId = handlerId
        block()
        defaultHandlerId = defaultHandlerIdBck
    }

    @HandlerDsl
    override fun handleShippingQuery(handlerId: String, handler: suspend (context(T) LocationShippingQueryHandler<T>.() -> Unit)) {
        allowedUpdates.add(UpdateType.SHIPPING_QUERY)
        shippingQueryHandler[handlerId] = LocationShippingQueryHandlerData(handler)
    }

    @HandlerDsl
    override fun handlePreCheckoutQuery(handlerId: String , handler: suspend (context(T) LocationPreCheckoutQueryHandler<T>.() -> Unit)) {
        allowedUpdates.add(UpdateType.PRE_CHECKOUT_QUERY)
        preCheckoutQueryHandler[handlerId] = LocationPreCheckoutQueryHandlerData(handler)
    }

    @HandlerDsl
    override fun handlePoll(handlerId: String, handler: suspend (context(T) LocationPollHandler<T>.() -> Unit)) {
        allowedUpdates.add(UpdateType.POLL)
        pollHandler[handlerId] = LocationPollHandlerData(handler)
    }

    @HandlerDsl
    override fun handlePollAnswer(handlerId: String, handler: suspend (context(T) LocationPollAnswerHandler<T>.() -> Unit)) {
        allowedUpdates.add(UpdateType.POLL_ANSWER)
        pollAnswerHandler[handlerId] = LocationPollAnswerHandlerData(handler)
    }

    @HandlerDsl
    override fun handleMyChatMember(handlerId: String, handler: suspend (context(T) LocationChatMemberHandler<T>.() -> Unit)) {
        allowedUpdates.add(UpdateType.MY_CHAT_MEMBER)
        myChatMemberHandler[handlerId] = LocationChatMemberHandlerData(handler)
    }

    @HandlerDsl
    override fun handleChatMember(handlerId: String, handler: suspend (context(T) LocationChatMemberHandler<T>.() -> Unit)) {
        allowedUpdates.add(UpdateType.CHAT_MEMBER)
        chatMemberHandler[handlerId] = LocationChatMemberHandlerData(handler)
    }

    @HandlerDsl
    override fun handleChatJoinRequest(handlerId: String, handler: suspend (context(T) LocationChatJoinRequestHandler<T>.() -> Unit)) {
        allowedUpdates.add(UpdateType.CHAT_JOIN_REQUEST)
        chatJoinRequestHandler[handlerId] = LocationChatJoinRequestHandlerData(handler)
    }
}