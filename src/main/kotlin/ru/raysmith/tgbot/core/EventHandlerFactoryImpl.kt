package ru.raysmith.tgbot.core

import org.slf4j.LoggerFactory
import ru.raysmith.tgbot.core.handler.*
import ru.raysmith.tgbot.model.bot.BotCommand
import ru.raysmith.tgbot.model.network.message.MessageType
import ru.raysmith.tgbot.model.network.updates.Update
import ru.raysmith.tgbot.model.network.updates.UpdateType
import ru.raysmith.tgbot.network.TelegramFileService
import ru.raysmith.tgbot.network.TelegramService
import ru.raysmith.tgbot.utils.datepicker.DatePicker

@DslMarker
annotation class HandlerDsl

@HandlerDsl
open class EventHandlerFactoryImpl : EventHandlerFactory {

    override val allowedUpdates = mutableSetOf<UpdateType>()

    private var unknownHandler: UnknownEventHandler.() -> Unit = { }
    private var messageHandler: (MessageHandler.() -> Unit)? = null
    private var commandHandler: (CommandHandler.() -> Unit)? = null
    private val callbackQueryHandler: MutableMap<String, CallbackQueryHandlerData> = mutableMapOf()
    private var chatMemberHandler: (ChatMemberHandler.() -> Unit)? = null
    private var preCheckoutQueryHandler: (PreCheckoutQueryHandler.() -> Unit)? = null
    private var shippingQueryHandler: (ShippingQueryHandler.() -> Unit)? = null
    private var inlineQueryHandler: (InlineQueryHandler.() -> Unit)? = null
    private var channelPostHandler: (ChannelPostHandler.() -> Unit)? = null

    private var alwaysAnswer: Boolean = false

    override fun getHandler(update: Update, service: TelegramService, fileService: TelegramFileService): EventHandler {
        return when {
            commandHandler != null && update.message?.type == MessageType.COMMAND ->
                CommandHandler(BotCommand(update.message.text!!), update.message, service, fileService, commandHandler!!)

            messageHandler != null && update.message?.type == MessageType.TEXT ->
                MessageHandler(update.message, service, fileService, messageHandler!!)

            update.callbackQuery != null ->
                CallbackQueryHandler(update.callbackQuery, alwaysAnswer, callbackQueryHandler, service, fileService)
    
            chatMemberHandler != null && update.myChatMember != null ->
                ChatMemberHandler(update.myChatMember, service, fileService, chatMemberHandler!!)
    
            chatMemberHandler != null && update.chatMember != null ->
                ChatMemberHandler(update.chatMember, service, fileService, chatMemberHandler!!)

            preCheckoutQueryHandler != null && update.preCheckoutQuery != null ->
                PreCheckoutQueryHandler(update.preCheckoutQuery, service, fileService, preCheckoutQueryHandler!!)

            shippingQueryHandler != null && update.shippingQuery != null ->
                ShippingQueryHandler(update.shippingQuery, service, fileService, shippingQueryHandler!!)

            inlineQueryHandler != null && update.inlineQuery != null ->
                InlineQueryHandler(update.inlineQuery, service, fileService, inlineQueryHandler!!)
            
            channelPostHandler != null && update.channelPost != null ->
                ChannelPostHandler(update.channelPost, service, fileService, channelPostHandler!!)

            else -> UnknownEventHandler(update, service, fileService, unknownHandler)
        }
    }
    
    @HandlerDsl
    fun handleUnknown(handler: UnknownEventHandler.() -> Unit) {
        unknownHandler = handler
    }

    @HandlerDsl
    fun handleChannelPost(handler: ChannelPostHandler.() -> Unit) {
        allowedUpdates.add(UpdateType.CHANNEL_POST)
        channelPostHandler = handler
    }

    @HandlerDsl
    fun handleMessage(handler: MessageHandler.() -> Unit) {
        allowedUpdates.add(UpdateType.MESSAGE)
        messageHandler = handler
    }
    
    @HandlerDsl
    fun handleCallbackQuery(
        alwaysAnswer: Boolean = false,
        handlerId: String = CallbackQueryHandler.HANDLER_ID,
        datePicker: DatePicker? = null,
        handler: (CallbackQueryHandler.() -> Unit)?
    ) {
        if (callbackQueryHandler.containsKey(handlerId)) {
            throw IllegalArgumentException("Callback handler with id '$handlerId' already registered")
        }

        this.alwaysAnswer = alwaysAnswer
        allowedUpdates.add(UpdateType.CALLBACK_QUERY)
        Bot.logger.debug("Register callbackQueryHandler '${handlerId}'")
        callbackQueryHandler[handlerId] = CallbackQueryHandlerData(handler, datePicker)
    }

    @HandlerDsl
    fun handleCommand(handler: CommandHandler.() -> Unit) {
        allowedUpdates.add(UpdateType.MESSAGE)
        commandHandler = handler
    }

    @HandlerDsl
    fun handleMyChatMember(handler: ChatMemberHandler.() -> Unit) {
        allowedUpdates.add(UpdateType.MY_CHAT_MEMBER)
        chatMemberHandler = handler
    }

    @HandlerDsl
    fun handleChatMember(handler: ChatMemberHandler.() -> Unit) {
        allowedUpdates.add(UpdateType.CHAT_MEMBER)
        chatMemberHandler = handler
    }

    @HandlerDsl
    fun handlePreCheckoutQuery(handler: PreCheckoutQueryHandler.() -> Unit) {
        allowedUpdates.add(UpdateType.PRE_CHECKOUT_QUERY)
        preCheckoutQueryHandler = handler
    }

    @HandlerDsl
    fun handleShippingQuery(handler: ShippingQueryHandler.() -> Unit) {
        allowedUpdates.add(UpdateType.SHIPPING_QUERY)
        shippingQueryHandler = handler
    }

    @HandlerDsl
    fun handleInlineQuery(handler: InlineQueryHandler.() -> Unit) {
        allowedUpdates.add(UpdateType.INLINE_QUERY)
        inlineQueryHandler = handler
    }
}