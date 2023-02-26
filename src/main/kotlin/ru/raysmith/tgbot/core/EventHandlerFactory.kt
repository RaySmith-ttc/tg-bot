package ru.raysmith.tgbot.core

import org.slf4j.LoggerFactory
import ru.raysmith.tgbot.core.handler.*
import ru.raysmith.tgbot.model.network.updates.Update
import ru.raysmith.tgbot.model.network.updates.UpdateType
import ru.raysmith.tgbot.network.TelegramFileService
import ru.raysmith.tgbot.network.TelegramService
import ru.raysmith.tgbot.utils.datepicker.DatePicker

@HandlerDsl
interface EventHandlerFactory {
    companion object {
        internal val logger = LoggerFactory.getLogger("event-handler-factory")
    }
    
    val allowedUpdates: Set<UpdateType>
    
    fun getHandler(update: Update, service: TelegramService, fileService: TelegramFileService): EventHandler
    
//    @HandlerDsl
//    fun handleCallbackQuery(
//        alwaysAnswer: Boolean = false,
//        handlerId: String = CallbackQueryHandler.HANDLER_ID,
//        datePicker: DatePicker? = null,
//        handler: (CallbackQueryHandler.() -> Unit)? = null
//    )
    
//    @HandlerDsl fun handleMessage(handler: MessageHandler.() -> Unit)
//    @HandlerDsl fun handleCommand(handler: CommandHandler.() -> Unit)
//    @HandlerDsl fun handleMyChatMember(handler: ChatMemberHandler.() -> Unit)
//    @HandlerDsl fun handleChatMember(handler: ChatMemberHandler.() -> Unit)
//    @HandlerDsl fun handlePreCheckoutQuery(handler: PreCheckoutQueryHandler.() -> Unit)
//    @HandlerDsl fun handleShippingQuery(handler: ShippingQueryHandler.() -> Unit)
//    @HandlerDsl fun handleInlineQuery(handler: InlineQueryHandler.() -> Unit)
//    @HandlerDsl fun handleUnknown(handler: UnknownEventHandler.() -> Unit)
}