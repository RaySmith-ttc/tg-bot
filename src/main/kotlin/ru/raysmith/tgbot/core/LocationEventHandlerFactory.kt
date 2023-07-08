package ru.raysmith.tgbot.core

import ru.raysmith.tgbot.core.handler.CallbackQueryHandler
import ru.raysmith.tgbot.core.handler.UnknownEventHandler
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
open class LocationEventHandlerFactory<T : LocationConfig>(val locationsWrapper: LocationsWrapper<T>) : EventHandlerFactory {
    
    protected open var defaultHandlerId = CallbackQueryHandler.HANDLER_ID
    
    override val allowedUpdates = mutableSetOf<UpdateType>()
    
    private var commandHandler: MutableMap<String, LocationCommandHandlerData<T>> = mutableMapOf()
    private var messageHandler: MutableMap<String, LocationMessageHandlerData<T>> = mutableMapOf()
    private val callbackQueryHandler: MutableMap<String, LocationCallbackQueryHandlerData<T>> = mutableMapOf()
    private var channelPostHandler: MutableMap<String, LocationChannelPostHandlerData<T>> = mutableMapOf()
    private var myChatMemberHandler: MutableMap<String, LocationChatMemberHandlerData<T>> = mutableMapOf()
    private var unknownHandler: UnknownEventHandler.() -> Unit = { }
    
    private var alwaysAnswer: Boolean = false
    
    override fun getHandler(
        update: Update, service: TelegramService, fileService: TelegramFileService
    ): EventHandler = when {
        update.message?.type == MessageType.COMMAND -> LocationCommandHandler(
                update, service, fileService, commandHandler, locationsWrapper
        )
        
        update.message?.type == MessageType.TEXT -> LocationMessageHandler(
                update, service, fileService, messageHandler, locationsWrapper
        )
    
        update.callbackQuery != null -> LocationCallbackQueryHandler(
                update, service, fileService, alwaysAnswer, callbackQueryHandler, locationsWrapper
        )
        
        update.channelPost != null -> LocationChannelPostHandler(
                update, service, fileService, channelPostHandler, locationsWrapper
        )
        
        update.myChatMember != null -> LocationChatMemberHandler(
                update, service, fileService, myChatMemberHandler, locationsWrapper
        )
        
        else -> UnknownEventHandler(update, service, fileService, unknownHandler)
    }
    
    fun handleUnknown(handler: UnknownEventHandler.() -> Unit) {
        unknownHandler = handler
    }
    
    
    @HandlerDsl
    fun handleCommand(handlerId: String = defaultHandlerId, handler: (context(T) LocationCommandHandler<T>.() -> Unit)) {
        allowedUpdates.add(UpdateType.MESSAGE)
        commandHandler[handlerId] = LocationCommandHandlerData(handler)
    }
    
    @HandlerDsl
    fun handleMessage(handlerId: String = defaultHandlerId, handler: (context(T) LocationMessageHandler<T>.() -> Unit)) {
        allowedUpdates.add(UpdateType.MESSAGE)
        messageHandler[handlerId] = LocationMessageHandlerData(handler)
    }
    
    @HandlerDsl
    fun handleChannelPost(handlerId: String = defaultHandlerId, handler: (context(T) LocationChannelPostHandler<T>.() -> Unit)) {
        allowedUpdates.add(UpdateType.CHANNEL_POST)
        channelPostHandler[handlerId] = LocationChannelPostHandlerData(handler)
    }
    
    @HandlerDsl
    fun handleMyChatMember(handlerId: String = defaultHandlerId, handler: (context(T) LocationChatMemberHandler<T>.() -> Unit)) {
        allowedUpdates.add(UpdateType.MY_CHAT_MEMBER)
        myChatMemberHandler[handlerId] = LocationChatMemberHandlerData(handler)
    }
    
    @HandlerDsl
    fun handleCallbackQuery(
        alwaysAnswer: Boolean = false,
        handlerId: String = CallbackQueryHandler.HANDLER_ID,
        datePicker: DatePicker? = null,
        handler: (context(T) LocationCallbackQueryHandler<T>.() -> Unit)?
    ) {
        if (callbackQueryHandler.containsKey(handlerId)) return

        this.alwaysAnswer = alwaysAnswer
        allowedUpdates.add(UpdateType.CALLBACK_QUERY)
        Bot.logger.debug("Register callbackQueryHandler '${handlerId}'")
        callbackQueryHandler[handlerId] = LocationCallbackQueryHandlerData(handler, datePicker)
    }
    
    internal fun withHandlerId(handlerId: String, block: LocationEventHandlerFactory<T>.() -> Unit) {
        val defaultHandlerIdBck = defaultHandlerId
        defaultHandlerId = handlerId
        block()
        defaultHandlerId = defaultHandlerIdBck
    }
    
//    @HandlerDsl
//    override fun handleMessage(handler: MessageHandler.() -> Unit) {
//        TODO("Not yet implemented")
//    }
//
//    @HandlerDsl
//    override fun handleCommand(handler: CommandHandler.() -> Unit) {
//        allowedUpdates.add(UpdateType.MESSAGE)
//        commandHandler[handlerId] = LocationCommandHandlerData(handler)
//    }
//
//    @HandlerDsl
//    override fun handleMyChatMember(handler: ChatMemberHandler.() -> Unit) {
//        TODO("Not yet implemented")
//    }
//
//    @HandlerDsl
//    override fun handleChatMember(handler: ChatMemberHandler.() -> Unit) {
//        TODO("Not yet implemented")
//    }
//
//    @HandlerDsl
//    override fun handlePreCheckoutQuery(handler: PreCheckoutQueryHandler.() -> Unit) {
//        TODO("Not yet implemented")
//    }
//
//    @HandlerDsl
//    override fun handleShippingQuery(handler: ShippingQueryHandler.() -> Unit) {
//        TODO("Not yet implemented")
//    }
//
//    @HandlerDsl
//    override fun handleInlineQuery(handler: InlineQueryHandler.() -> Unit) {
//        TODO("Not yet implemented")
//    }
}