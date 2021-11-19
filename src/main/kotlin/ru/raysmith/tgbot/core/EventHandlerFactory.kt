package ru.raysmith.tgbot.core

import ru.raysmith.tgbot.core.handler.*
import ru.raysmith.tgbot.model.network.User
import ru.raysmith.tgbot.model.network.message.MessageType
import ru.raysmith.tgbot.model.network.updates.Update
import ru.raysmith.tgbot.model.network.updates.UpdateType
import ru.raysmith.tgbot.utils.DatePicker
import ru.raysmith.tgbot.utils.addIfNotContains
import java.util.*

data class CallbackQueryHandlerData(val handler: (CallbackQueryHandler.() -> Unit)? = null, val datePicker: DatePicker? = null)
object EventHandlerFactory {

    private val allowedUpdates = mutableListOf<UpdateType>()

    private lateinit var unknownHandler: UnknownEventHandler.() -> Unit
    private lateinit var messageHandler: MessageHandler.() -> Unit
    private lateinit var commandHandler: CommandHandler.() -> Unit
    private val callbackQueryHandler: MutableMap<String, CallbackQueryHandlerData> = mutableMapOf()
    private lateinit var chatMemberHandler: ChatMemberHandler.() -> Unit
    private lateinit var preCheckoutQueryHandler: PreCheckoutQueryHandler.() -> Unit
    private lateinit var shippingQueryHandler: ShippingQueryHandler.() -> Unit

    private var alwaysAnswer: Boolean = false

    fun getHandler(update: Update): EventHandler {
        return when {

            // message
            ::commandHandler.isInitialized && update.message != null && update.message.type == MessageType.COMMAND ->
                CommandHandler(BotCommand(update.message.text!!), update.message.from!!, update, commandHandler)

            ::messageHandler.isInitialized && update.message != null && update.message.type == MessageType.TEXT ->
                MessageHandler(update.message, update.message.from!!, messageHandler)

            update.callbackQuery != null -> CallbackQueryHandler(
                update.callbackQuery,
                alwaysAnswer,
                callbackQueryHandler
            )

            update.myChatMember != null -> ChatMemberHandler(update.myChatMember, chatMemberHandler)

            update.chatMember != null -> ChatMemberHandler(update.chatMember, chatMemberHandler)

            ::preCheckoutQueryHandler.isInitialized && update.preCheckoutQuery != null -> PreCheckoutQueryHandler(update.preCheckoutQuery, preCheckoutQueryHandler)

            ::shippingQueryHandler.isInitialized && update.shippingQuery != null -> ShippingQueryHandler(update.shippingQuery, shippingQueryHandler)

            else -> UnknownEventHandler(update, unknownHandler)
        }
    }

    fun getAllowedUpdateTypes(): List<UpdateType> = allowedUpdates

    fun handleUnknown(handler: UnknownEventHandler.() -> Unit) {
        unknownHandler = handler
    }

    fun handleMessage(handler: MessageHandler.() -> Unit) {
        allowedUpdates.addIfNotContains(UpdateType.MESSAGE)
        messageHandler = handler
    }

    fun handleCallbackQuery(alwaysAnswer: Boolean = false, handlerId: String = CallbackQueryHandler.HANDLER_ID, datePicker: DatePicker? = null, handler: (CallbackQueryHandler.() -> Unit)? = null) {
        this.alwaysAnswer = alwaysAnswer
        allowedUpdates.addIfNotContains(UpdateType.CALLBACK_QUERY)
        callbackQueryHandler[
                if (callbackQueryHandler.containsKey(handlerId)) UUID.randomUUID().toString()
                else CallbackQueryHandler.HANDLER_ID
        ] = CallbackQueryHandlerData(handler, datePicker)
    }

    fun handleCommand(handler: CommandHandler.() -> Unit) {
        allowedUpdates.addIfNotContains(UpdateType.MESSAGE)
        commandHandler = handler
    }

    fun handleMyChatMember(handler: ChatMemberHandler.() -> Unit) {
        allowedUpdates.addIfNotContains(UpdateType.MY_CHAT_MEMBER)
        chatMemberHandler = handler
    }

    fun handleChatMember(handler: ChatMemberHandler.() -> Unit) {
        allowedUpdates.addIfNotContains(UpdateType.CHAT_MEMBER)
        chatMemberHandler = handler
    }

    fun handlePreCheckoutQuery(handler: PreCheckoutQueryHandler.() -> Unit) {
        allowedUpdates.addIfNotContains(UpdateType.PRE_CHECKOUT_QUERY)
        preCheckoutQueryHandler = handler
    }

    fun handleShippingQuery(handler: ShippingQueryHandler.() -> Unit) {
        allowedUpdates.addIfNotContains(UpdateType.SHIPPING_QUERY)
        shippingQueryHandler = handler
    }

}

interface ICommand {
    val text: String
}

class BotCommand(commandText: String) : ICommand {
    override val text: String = commandText

    companion object {
        const val START = "/start"
    }
}

class CommandHandler(val command: ICommand, val user: User, val update: Update, val handler: CommandHandler.() -> Unit) : EventHandler, ISender, IEditor {
    override suspend fun handle() = handler()

    override var chatId: String? = user.id.toString()
    override var messageId: Long? = null
    override var inlineMessageId: String? = null


}