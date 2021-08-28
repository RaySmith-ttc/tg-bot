package ru.raysmith.tgbot.core

import ru.raysmith.tgbot.core.handler.CallbackQueryHandler
import ru.raysmith.tgbot.core.handler.ChatMemberHandler
import ru.raysmith.tgbot.core.handler.MessageHandler
import ru.raysmith.tgbot.core.handler.UnknownEventHandler
import ru.raysmith.tgbot.model.network.User
import ru.raysmith.tgbot.model.network.message.MessageType
import ru.raysmith.tgbot.model.network.updates.Update
import ru.raysmith.tgbot.model.network.updates.UpdateType
import ru.raysmith.tgbot.utils.addIfNotContains

object EventHandlerFactory {

    private val allowedUpdates = mutableListOf<UpdateType>()

    private lateinit var unknownHandler: UnknownEventHandler.() -> Unit
    private lateinit var messageHandler: MessageHandler.() -> Unit
    private lateinit var commandHandler: CommandHandler.() -> Unit
    private lateinit var callbackQueryHandler: CallbackQueryHandler.() -> Unit
    private lateinit var chatMemberHandler: ChatMemberHandler.() -> Unit

    private var alwaysAnswer: Boolean = false

    fun getHandler(update: Update): EventHandler {
        return when {

            // message
            ::commandHandler.isInitialized && update.message != null && update.message.type == MessageType.COMMAND ->
                CommandHandler(BotCommand(update.message.text!!), update.message.from!!, update, commandHandler)

            ::messageHandler.isInitialized && update.message != null && update.message.type == MessageType.TEXT ->
                MessageHandler(update.message, update.message.from!!, messageHandler)

            ::callbackQueryHandler.isInitialized && update.callbackQuery != null -> CallbackQueryHandler(
                update.callbackQuery,
                alwaysAnswer,
                callbackQueryHandler
            )

            ::callbackQueryHandler.isInitialized && update.myChatMember != null -> ChatMemberHandler(update.myChatMember, chatMemberHandler)

            ::callbackQueryHandler.isInitialized && update.chatMember != null -> ChatMemberHandler(update.chatMember, chatMemberHandler)

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

    fun handleCallbackQuery(alwaysAnswer: Boolean = false, handler: CallbackQueryHandler.() -> Unit) {
        this.alwaysAnswer = alwaysAnswer
        allowedUpdates.addIfNotContains(UpdateType.CALLBACK_QUERY)
        callbackQueryHandler = handler
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