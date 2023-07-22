package ru.raysmith.tgbot.core.handler

import ru.raysmith.tgbot.core.Bot
import ru.raysmith.tgbot.core.handler.base.CallbackQueryHandler
import ru.raysmith.tgbot.core.handler.base.UnknownEventHandler
import ru.raysmith.tgbot.core.handler.location.*
import ru.raysmith.tgbot.model.network.message.MessageType
import ru.raysmith.tgbot.model.network.updates.Update
import ru.raysmith.tgbot.model.network.updates.UpdateType
import ru.raysmith.tgbot.network.TelegramFileService
import ru.raysmith.tgbot.network.TelegramService
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
    private val editedChannelPost: MutableMap<String, LocationEditedChannelPostHandlerData<T>> = mutableMapOf()
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

    private var unknownHandler: UnknownEventHandler.() -> Unit = { }
    
    override fun getHandler(
        update: Update, service: TelegramService, fileService: TelegramFileService
    ): EventHandler = when(update.type) {
        UpdateType.MESSAGE -> when(update.message!!.type) {
            MessageType.COMMAND -> LocationCommandHandler(update, service, fileService, commandHandler, locationsWrapper)
            MessageType.TEXT -> LocationMessageHandler(update, service, fileService, messageHandler, locationsWrapper)
        }

        UpdateType.EDITED_MESSAGE ->LocationEditedMessageHandler(
            update, service, fileService, editedMessageHandler, locationsWrapper
        )

        UpdateType.CHANNEL_POST -> LocationChannelPostHandler(
                update, service, fileService, channelPostHandler, locationsWrapper
        )

        UpdateType.EDITED_CHANNEL_POST -> LocationEditedChannelPostHandler(
                update, service, fileService, editedChannelPost, locationsWrapper
        )

        UpdateType.INLINE_QUERY -> LocationInlineQueryHandler(
            update, service, fileService, inlineQueryHandler, locationsWrapper
        )

        UpdateType.CHOSEN_INLINE_RESULT -> LocationChosenInlineQueryHandler(
            update, service, fileService, chosenInlineQueryHandler, locationsWrapper
        )

        UpdateType.CALLBACK_QUERY -> LocationCallbackQueryHandler(
            update, service, fileService, callbackQueryHandler, locationsWrapper
        )

        UpdateType.SHIPPING_QUERY -> LocationShippingQueryHandler(
            update, service, fileService, shippingQueryHandler, locationsWrapper
        )

        UpdateType.PRE_CHECKOUT_QUERY -> LocationPreCheckoutQueryHandler(
            update, service, fileService, preCheckoutQueryHandler, locationsWrapper
        )

        UpdateType.POLL -> LocationPollHandler(
            update, service, fileService, pollHandler, locationsWrapper
        )

        UpdateType.POLL_ANSWER -> LocationPollAnswerHandler(
            update, service, fileService, pollAnswerHandler, locationsWrapper
        )

        UpdateType.MY_CHAT_MEMBER -> LocationChatMemberHandler(
                update, service, fileService, myChatMemberHandler, locationsWrapper
        )

        UpdateType.CHAT_MEMBER -> LocationChatMemberHandler(
                update, service, fileService, chatMemberHandler, locationsWrapper
        )

        UpdateType.CHAT_JOIN_REQUEST -> LocationChatJoinRequestHandler(
                update, service, fileService, chatJoinRequestHandler, locationsWrapper
        )
        
        null -> UnknownEventHandler(update, service, fileService, unknownHandler)
    }
    
    fun handleUnknown(handler: UnknownEventHandler.() -> Unit) {
        unknownHandler = handler
    }


    @HandlerDsl
    fun handleMessage(handlerId: String = defaultHandlerId, handler: (context(T) LocationMessageHandler<T>.() -> Unit)) {
        allowedUpdates.add(UpdateType.MESSAGE)
        messageHandler[handlerId] = LocationMessageHandlerData(handler)
    }

    @HandlerDsl
    fun handleCommand(handlerId: String = defaultHandlerId, handler: (context(T) LocationCommandHandler<T>.() -> Unit)) {
        allowedUpdates.add(UpdateType.MESSAGE)
        commandHandler[handlerId] = LocationCommandHandlerData(handler)
    }

    @HandlerDsl
    fun handleEditedMessage(handlerId: String = defaultHandlerId, handler: (context(T) LocationEditedMessageHandler<T>.() -> Unit)) {
        allowedUpdates.add(UpdateType.EDITED_MESSAGE)
        editedMessageHandler[handlerId] = LocationEditMessageHandlerData(handler)
    }

    @HandlerDsl
    fun handleChannelPost(handlerId: String = defaultHandlerId, handler: (context(T) LocationChannelPostHandler<T>.() -> Unit)) {
        allowedUpdates.add(UpdateType.CHANNEL_POST)
        channelPostHandler[handlerId] = LocationChannelPostHandlerData(handler)
    }

    @HandlerDsl
    fun handleEditedChannelPost(handlerId: String = defaultHandlerId, handler: (context(T) LocationEditedChannelPostHandler<T>.() -> Unit)) {
        allowedUpdates.add(UpdateType.EDITED_CHANNEL_POST)
        editedChannelPost[handlerId] = LocationEditedChannelPostHandlerData(handler)
    }

    @HandlerDsl
    fun handleInlineQuery(handlerId: String = defaultHandlerId, handler: (context(T) LocationInlineQueryHandler<T>.() -> Unit)) {
        allowedUpdates.add(UpdateType.INLINE_QUERY)
        inlineQueryHandler[handlerId] = LocationInlineQueryHandlerData(handler)
    }

    @HandlerDsl
    fun handleChosenInlineQuery(handlerId: String = defaultHandlerId, handler: (context(T) LocationChosenInlineQueryHandler<T>.() -> Unit)) {
        allowedUpdates.add(UpdateType.CHOSEN_INLINE_RESULT)
        chosenInlineQueryHandler[handlerId] = LocationChosenInlineQueryHandlerData(handler)
    }

    @HandlerDsl
    fun handleCallbackQuery(
        alwaysAnswer: Boolean = Bot.config.alwaysAnswerCallback,
        handlerId: String = CallbackQueryHandler.HANDLER_ID,
        datePicker: DatePicker? = null,
        handler: (context(T) LocationCallbackQueryHandler<T>.() -> Unit)?
    ) {
        if (callbackQueryHandler.containsKey(handlerId)) return
        allowedUpdates.add(UpdateType.CALLBACK_QUERY)
        Bot.logger.debug("Register callbackQueryHandler '${handlerId}'")
        callbackQueryHandler[handlerId] = LocationCallbackQueryHandlerData(handler, datePicker, alwaysAnswer)
    }

    internal fun withHandlerId(handlerId: String, block: LocationEventHandlerFactory<T>.() -> Unit) {
        val defaultHandlerIdBck = defaultHandlerId
        defaultHandlerId = handlerId
        block()
        defaultHandlerId = defaultHandlerIdBck
    }

    @HandlerDsl
    fun handleShippingQuery(handlerId: String = defaultHandlerId, handler: (context(T) LocationShippingQueryHandler<T>.() -> Unit)) {
        allowedUpdates.add(UpdateType.SHIPPING_QUERY)
        shippingQueryHandler[handlerId] = LocationShippingQueryHandlerData(handler)
    }

    @HandlerDsl
    fun handlePreCheckoutQuery(handlerId: String = defaultHandlerId, handler: (context(T) LocationPreCheckoutQueryHandler<T>.() -> Unit)) {
        allowedUpdates.add(UpdateType.PRE_CHECKOUT_QUERY)
        preCheckoutQueryHandler[handlerId] = LocationPreCheckoutQueryHandlerData(handler)
    }

    @HandlerDsl
    fun handlePoll(handlerId: String = defaultHandlerId, handler: (context(T) LocationPollHandler<T>.() -> Unit)) {
        allowedUpdates.add(UpdateType.POLL)
        pollHandler[handlerId] = LocationPollHandlerData(handler)
    }

    @HandlerDsl
    fun handlePollAnswer(handlerId: String = defaultHandlerId, handler: (context(T) LocationPollAnswerHandler<T>.() -> Unit)) {
        allowedUpdates.add(UpdateType.POLL_ANSWER)
        pollAnswerHandler[handlerId] = LocationPollAnswerHandlerData(handler)
    }
    
    @HandlerDsl
    fun handleMyChatMember(handlerId: String = defaultHandlerId, handler: (context(T) LocationChatMemberHandler<T>.() -> Unit)) {
        allowedUpdates.add(UpdateType.MY_CHAT_MEMBER)
        myChatMemberHandler[handlerId] = LocationChatMemberHandlerData(handler)
    }

    @HandlerDsl
    fun handleChatMember(handlerId: String = defaultHandlerId, handler: (context(T) LocationChatMemberHandler<T>.() -> Unit)) {
        allowedUpdates.add(UpdateType.CHAT_MEMBER)
        chatMemberHandler[handlerId] = LocationChatMemberHandlerData(handler)
    }

    @HandlerDsl
    fun handleChatJoinRequest(handlerId: String = defaultHandlerId, handler: (context(T) LocationChatJoinRequestHandler<T>.() -> Unit)) {
        allowedUpdates.add(UpdateType.CHAT_JOIN_REQUEST)
        chatJoinRequestHandler[handlerId] = LocationChatJoinRequestHandlerData(handler)
    }
}