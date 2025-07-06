package ru.raysmith.tgbot.core.handler.location

import ru.raysmith.tgbot.core.Bot
import ru.raysmith.tgbot.core.BotContext
import ru.raysmith.tgbot.core.handler.LocationHandler
import ru.raysmith.tgbot.core.handler.base.CommandHandler
import ru.raysmith.tgbot.model.bot.BotCommand
import ru.raysmith.tgbot.model.network.updates.Update
import ru.raysmith.tgbot.utils.locations.LocationFlowContext
import ru.raysmith.tgbot.utils.locations.LocationsWrapper

data class LocationCommandHandlerData<LFC : LocationFlowContext>(
    val handler: (suspend context(LFC) LocationCommandHandler<LFC>.() -> Unit)? = null
)

class LocationCommandHandler<LFC : LocationFlowContext>(
    override val update: Update, bot: Bot,
    private val handlerData: List<LocationCommandHandlerData<LFC>>,
    override val locationsWrapper: LocationsWrapper<LFC>
) : CommandHandler(BotCommand(update.message!!.text!!), update.message, bot), LocationHandler<LFC, CommandHandler> {
    
    override val locationFlowContext by lazy { locationFlowContext() }
    override suspend fun handle() {
        for (it in handlerData) {
            it.handler?.let { it1 -> it1(locationFlowContext, this) }?.also {
                handled = true
            }
        }
        handleLocalFeatures(handled)
    }

    suspend fun isCommand(value: String, equalHandler: suspend context(LFC) LocationCommandHandler<LFC>.(argsString: String?) -> Unit) {
        if (command.body == value) {
            with(locationFlowContext) {
                equalHandler(command.argsString)
            }
        }
    }

    override suspend fun <R> withBot(bot: Bot, block: suspend BotContext<CommandHandler>.() -> R): R {
        return LocationCommandHandler(update, bot, handlerData, locationsWrapper).let {
            this.block()
        }
    }
}