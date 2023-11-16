package ru.raysmith.tgbot.core.handler

import io.ktor.client.*
import ru.raysmith.tgbot.core.Bot
import ru.raysmith.tgbot.core.handler.base.CallbackQueryHandler
import ru.raysmith.tgbot.core.handler.base.UnknownEventHandler
import ru.raysmith.tgbot.core.handler.location.*
import ru.raysmith.tgbot.model.network.message.MessageType
import ru.raysmith.tgbot.model.network.updates.Update
import ru.raysmith.tgbot.model.network.updates.UpdateType
import ru.raysmith.tgbot.utils.datepicker.DatePicker
import ru.raysmith.tgbot.utils.locations.LocationConfig
import ru.raysmith.tgbot.utils.locations.LocationsWrapper

@HandlerDsl
open class LocationEventHandlerFactory<T : LocationConfig>(val locationsWrapper: LocationsWrapper<T>) :
    EventHandlerFactory {
    
    protected open var defaultHandlerId = CallbackQueryHandler.HANDLER_ID
    
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
    
    override fun getHandler(
        update: Update, client: HttpClient
    ): EventHandler = when(update.type) {
        UpdateType.MESSAGE -> when(update.message!!.type) {
            MessageType.COMMAND -> LocationCommandHandler(update, client, commandHandler, locationsWrapper)
            MessageType.TEXT -> LocationMessageHandler(update, client, messageHandler, locationsWrapper)
        }

        UpdateType.EDITED_MESSAGE -> LocationEditedMessageHandler(
            update, client, editedMessageHandler, locationsWrapper
        )

        UpdateType.CHANNEL_POST -> LocationChannelPostHandler(
                update, client, channelPostHandler, locationsWrapper
        )

        UpdateType.EDITED_CHANNEL_POST -> LocationEditedChannelPostHandler(
                update, client, editedChannelPostHandler, locationsWrapper
        )

        UpdateType.INLINE_QUERY -> LocationInlineQueryHandler(
            update, client, inlineQueryHandler, locationsWrapper
        )

        UpdateType.CHOSEN_INLINE_RESULT -> LocationChosenInlineQueryHandler(
            update, client, chosenInlineQueryHandler, locationsWrapper
        )

        UpdateType.CALLBACK_QUERY -> LocationCallbackQueryHandler(
            update, client, callbackQueryHandler, locationsWrapper
        )

        UpdateType.SHIPPING_QUERY -> LocationShippingQueryHandler(
            update, client, shippingQueryHandler, locationsWrapper
        )

        UpdateType.PRE_CHECKOUT_QUERY -> LocationPreCheckoutQueryHandler(
            update, client, preCheckoutQueryHandler, locationsWrapper
        )

        UpdateType.POLL -> LocationPollHandler(
            update, client, pollHandler, locationsWrapper
        )

        UpdateType.POLL_ANSWER -> LocationPollAnswerHandler(
            update, client, pollAnswerHandler, locationsWrapper
        )

        UpdateType.MY_CHAT_MEMBER -> LocationChatMemberHandler(
                update, client, myChatMemberHandler, locationsWrapper
        )

        UpdateType.CHAT_MEMBER -> LocationChatMemberHandler(
                update, client, chatMemberHandler, locationsWrapper
        )

        UpdateType.CHAT_JOIN_REQUEST -> LocationChatJoinRequestHandler(
                update, client, chatJoinRequestHandler, locationsWrapper
        )
        
        null -> UnknownEventHandler(update, client, unknownHandler)
    }

    fun handleUnknown(handler: suspend UnknownEventHandler.() -> Unit) {
        unknownHandler = handler
    }


    @HandlerDsl
    fun handleMessage(handlerId: String = defaultHandlerId, handler: suspend (context(T) LocationMessageHandler<T>.() -> Unit)) {
        allowedUpdates.add(UpdateType.MESSAGE)
        messageHandler[handlerId] = LocationMessageHandlerData(handler)
    }

    @HandlerDsl
    fun handleCommand(handlerId: String = defaultHandlerId, handler:suspend (context(T) LocationCommandHandler<T>.() -> Unit)) {
        allowedUpdates.add(UpdateType.MESSAGE)
        commandHandler[handlerId] = LocationCommandHandlerData(handler)
    }

    @HandlerDsl
    fun handleEditedMessage(handlerId: String = defaultHandlerId, handler: suspend (context(T) LocationEditedMessageHandler<T>.() -> Unit)) {
        allowedUpdates.add(UpdateType.EDITED_MESSAGE)
        editedMessageHandler[handlerId] = LocationEditMessageHandlerData(handler)
    }

    @HandlerDsl
    fun handleChannelPost(handlerId: String = defaultHandlerId, handler: suspend (context(T) LocationChannelPostHandler<T>.() -> Unit)) {
        allowedUpdates.add(UpdateType.CHANNEL_POST)
        channelPostHandler[handlerId] = LocationChannelPostHandlerData(handler)
    }

    @HandlerDsl
    fun handleEditedChannelPost(handlerId: String = defaultHandlerId, handler: suspend (context(T) LocationEditedChannelPostHandler<T>.() -> Unit)) {
        allowedUpdates.add(UpdateType.EDITED_CHANNEL_POST)
        editedChannelPostHandler[handlerId] = LocationEditedChannelPostHandlerData(handler)
    }

    @HandlerDsl
    fun handleInlineQuery(handlerId: String = defaultHandlerId, handler: suspend (context(T) LocationInlineQueryHandler<T>.() -> Unit)) {
        allowedUpdates.add(UpdateType.INLINE_QUERY)
        inlineQueryHandler[handlerId] = LocationInlineQueryHandlerData(handler)
    }

    @HandlerDsl
    fun handleChosenInlineQuery(handlerId: String = defaultHandlerId, handler: suspend (context(T) LocationChosenInlineQueryHandler<T>.() -> Unit)) {
        allowedUpdates.add(UpdateType.CHOSEN_INLINE_RESULT)
        chosenInlineQueryHandler[handlerId] = LocationChosenInlineQueryHandlerData(handler)
    }

    @HandlerDsl
    fun handleCallbackQuery(
        alwaysAnswer: Boolean = Bot.config.alwaysAnswerCallback,
        handlerId: String = CallbackQueryHandler.HANDLER_ID,
        datePicker: DatePicker? = null,
        handler: (suspend context(T) LocationCallbackQueryHandler<T>.() -> Unit)?
    ) {
        if (callbackQueryHandler.containsKey(handlerId)) return
        allowedUpdates.add(UpdateType.CALLBACK_QUERY)
        Bot.logger.debug("Register callbackQueryHandler '${handlerId}'")
        callbackQueryHandler[handlerId] = LocationCallbackQueryHandlerData(handler, datePicker, alwaysAnswer)
    }

    internal suspend fun withHandlerId(handlerId: String, block: suspend LocationEventHandlerFactory<T>.() -> Unit) {
        val defaultHandlerIdBck = defaultHandlerId
        defaultHandlerId = handlerId
        block()
        defaultHandlerId = defaultHandlerIdBck
    }

    @HandlerDsl
    fun handleShippingQuery(handlerId: String = defaultHandlerId, handler: suspend (context(T) LocationShippingQueryHandler<T>.() -> Unit)) {
        allowedUpdates.add(UpdateType.SHIPPING_QUERY)
        shippingQueryHandler[handlerId] = LocationShippingQueryHandlerData(handler)
    }

    @HandlerDsl
    fun handlePreCheckoutQuery(handlerId: String = defaultHandlerId, handler: suspend (context(T) LocationPreCheckoutQueryHandler<T>.() -> Unit)) {
        allowedUpdates.add(UpdateType.PRE_CHECKOUT_QUERY)
        preCheckoutQueryHandler[handlerId] = LocationPreCheckoutQueryHandlerData(handler)
    }

    @HandlerDsl
    fun handlePoll(handlerId: String = defaultHandlerId, handler: suspend (context(T) LocationPollHandler<T>.() -> Unit)) {
        allowedUpdates.add(UpdateType.POLL)
        pollHandler[handlerId] = LocationPollHandlerData(handler)
    }

    @HandlerDsl
    fun handlePollAnswer(handlerId: String = defaultHandlerId, handler: suspend (context(T) LocationPollAnswerHandler<T>.() -> Unit)) {
        allowedUpdates.add(UpdateType.POLL_ANSWER)
        pollAnswerHandler[handlerId] = LocationPollAnswerHandlerData(handler)
    }
    
    @HandlerDsl
    fun handleMyChatMember(handlerId: String = defaultHandlerId, handler: suspend (context(T) LocationChatMemberHandler<T>.() -> Unit)) {
        allowedUpdates.add(UpdateType.MY_CHAT_MEMBER)
        myChatMemberHandler[handlerId] = LocationChatMemberHandlerData(handler)
    }

    @HandlerDsl
    fun handleChatMember(handlerId: String = defaultHandlerId, handler: suspend (context(T) LocationChatMemberHandler<T>.() -> Unit)) {
        allowedUpdates.add(UpdateType.CHAT_MEMBER)
        chatMemberHandler[handlerId] = LocationChatMemberHandlerData(handler)
    }

    @HandlerDsl
    fun handleChatJoinRequest(handlerId: String = defaultHandlerId, handler: suspend (context(T) LocationChatJoinRequestHandler<T>.() -> Unit)) {
        allowedUpdates.add(UpdateType.CHAT_JOIN_REQUEST)
        chatJoinRequestHandler[handlerId] = LocationChatJoinRequestHandlerData(handler)
    }
}