package ru.raysmith.tgbot.core.handler.location

import io.ktor.client.*
import ru.raysmith.tgbot.core.Bot
import ru.raysmith.tgbot.core.BotContext
import ru.raysmith.tgbot.core.handler.HandlerDsl
import ru.raysmith.tgbot.core.handler.LocationHandler
import ru.raysmith.tgbot.core.handler.base.CommandHandler
import ru.raysmith.tgbot.model.bot.BotCommand
import ru.raysmith.tgbot.model.network.updates.Update
import ru.raysmith.tgbot.utils.locations.LocationConfig
import ru.raysmith.tgbot.utils.locations.LocationsWrapper

data class LocationCommandHandlerData<T : LocationConfig>(
    val handler: (context(T) LocationCommandHandler<T>.() -> Unit)? = null
)

@HandlerDsl
class LocationCommandHandler<T : LocationConfig>(
    override val update: Update, client: HttpClient,
    private val handlerData: Map<String, LocationCommandHandlerData<T>>,
    override val locationsWrapper: LocationsWrapper<T>
) : CommandHandler(BotCommand(update.message!!.text!!), update.message, client), LocationHandler<T> {
    
    override val config by lazy { config() }
    override fun handle() {
        handlerData.forEach {
            it.value.handler?.let { it1 ->
                it1(config, this)
            }
        }
    }

    fun isCommand(value: String, equalHandler: LocationCommandHandler<T>.(argsString: String?) -> Unit) {
        if (command.body == value) {
            equalHandler(command.argsString)
        }
    }
    
    override fun <R> withBot(bot: Bot, block: BotContext<CommandHandler>.() -> R): R {
        return LocationCommandHandler(update, bot.client, handlerData, locationsWrapper).let {
            this.block()
        }
    }
}