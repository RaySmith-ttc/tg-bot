package ru.raysmith.tgbot.core

import ru.raysmith.tgbot.model.network.User
import ru.raysmith.tgbot.model.network.message.MessageType
import ru.raysmith.tgbot.model.network.updates.Update
import ru.raysmith.tgbot.model.network.updates.UpdateType
import ru.raysmith.tgbot.utils.addIFNotContains

object EventHandlerFactory {

    private val allowedUpdates = mutableListOf<UpdateType>()

    private lateinit var messageHandler: MessageHandler.() -> Unit
    private lateinit var commandHandler: CommandHandler.() -> Unit
    private lateinit var callbackQueryHandler: CallbackQueryHandler.() -> Unit
    private var alwaysAnswer: Boolean = false

    fun getHandler(update: Update): EventHandler {
        return when {
            ::commandHandler.isInitialized && update.message != null && update.message.type == MessageType.COMMAND -> CommandHandler(BotCommand(update.message.text!!), update.message.from!!, commandHandler)
            ::messageHandler.isInitialized && update.message != null && update.message.type == MessageType.TEXT -> MessageHandler(update.message, update.message.from!!, messageHandler)
            ::callbackQueryHandler.isInitialized && update.callbackQuery != null -> CallbackQueryHandler(
                update.callbackQuery,
                alwaysAnswer,
                callbackQueryHandler
            )
            else -> EmptyEventHandler()
        }
    }

    fun getAllowedUpdateTypes(): List<UpdateType> = allowedUpdates

    fun handleMessage(handler: MessageHandler.() -> Unit) {
        allowedUpdates.addIFNotContains(UpdateType.MESSAGE)
        messageHandler = handler
    }

    fun handleCallbackQuery(alwaysAnswer: Boolean = false, handler: CallbackQueryHandler.() -> Unit) {
        this.alwaysAnswer = alwaysAnswer
        allowedUpdates.addIFNotContains(UpdateType.CALLBACK_QUERY)
        callbackQueryHandler = handler
    }

    fun handleCommand(handler: CommandHandler.() -> Unit) {
        allowedUpdates.addIFNotContains(UpdateType.MESSAGE)
        commandHandler = handler
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

class CommandHandler(val command: ICommand, val user: User, val handler: CommandHandler.() -> Unit) : ISender, EventHandler {
    override suspend fun handle() = handler()

    override var chatId: String? = user.id.toString()
    override var messageId: Long? = null
    override var inlineMessageId: String? = null


}