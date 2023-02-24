package ru.raysmith.tgbot.core.handler

import ru.raysmith.tgbot.core.*
import ru.raysmith.tgbot.model.bot.BotCommand
import ru.raysmith.tgbot.model.network.CallbackQuery
import ru.raysmith.tgbot.model.network.message.Message
import ru.raysmith.tgbot.model.network.updates.Update
import ru.raysmith.tgbot.network.TelegramFileService
import ru.raysmith.tgbot.network.TelegramService
import ru.raysmith.tgbot.utils.BotContextDsl
import ru.raysmith.tgbot.utils.handleAll
import ru.raysmith.tgbot.utils.locations.LocationConfig
import ru.raysmith.tgbot.utils.locations.LocationsWrapper

@HandlerDsl
open class CommandHandler(
    val command: BotCommand, val message: Message,
    override val service: TelegramService, override val fileService: TelegramFileService,
    val handler: CommandHandler.() -> Unit = { }
) : EventHandler, BotContext<CommandHandler> {

    override fun getChatId() = message.chat.id
    override fun getChatIdOrThrow() = message.chat.id
    override var messageId: Int? = null
    override var inlineMessageId: String? = null

    override suspend fun handle() = handler()

    @BotContextDsl
    override fun withBot(bot: Bot, block: BotContext<CommandHandler>.() -> Any) {
        CommandHandler(command, message, bot.service, bot.fileService, handler).apply {
            this.block()
        }
    }
}

inline fun CommandHandler.isCommand(value: String, equalHandler: CommandHandler.(argsString: String?) -> Unit) {
    if (command.body == value) {
        equalHandler(command.argsString)
    }
}

class LocationCommandHandler<T : LocationConfig>(
    override val update: Update, override val service: TelegramService, override val fileService: TelegramFileService,
    private val handlerData: Map<String, LocationCommendHandlerData<T>>,
    override val locationsWrapper: LocationsWrapper<T>
) : CommandHandler(BotCommand(update.message!!.text!!), update.message, service, fileService), LocationHandler<T> {
    override fun toLocation(name: String) {
        val config = locationsWrapper.configCreator(update)
        val location = locationsWrapper.onToLocation(config, name)
        location.onEnter(this)
    }
    
    override suspend fun handle() {
        handlerData.forEach {
            it.value.handler?.let { it1 -> it1(config()) }
        }
    }
    
    fun isCommand(value: String, equalHandler: LocationCommandHandler<T>.(argsString: String?) -> Unit) {
        if (command.body == value) {
            equalHandler(command.argsString)
        }
    }
}