package ru.raysmith.tgbot.core.handler.location

import ru.raysmith.tgbot.core.Bot
import ru.raysmith.tgbot.core.BotContext
import ru.raysmith.tgbot.core.handler.HandlerDsl
import ru.raysmith.tgbot.core.handler.LocationHandler
import ru.raysmith.tgbot.core.handler.base.PollAnswerHandler
import ru.raysmith.tgbot.model.network.updates.Update
import ru.raysmith.tgbot.network.TelegramFileService
import ru.raysmith.tgbot.network.TelegramService
import ru.raysmith.tgbot.utils.locations.LocationConfig
import ru.raysmith.tgbot.utils.locations.LocationsWrapper

data class LocationPollAnswerHandlerData<T : LocationConfig>(
    val handler: (context(T) LocationPollAnswerHandler<T>.() -> Unit)? = null
)

@HandlerDsl
class LocationPollAnswerHandler<T : LocationConfig>(
    override val update: Update, service: TelegramService, fileService: TelegramFileService,
    private val handlerData: MutableMap<String, LocationPollAnswerHandlerData<T>>,
    override val locationsWrapper: LocationsWrapper<T>
) : PollAnswerHandler(update.pollAnswer!!, service, fileService), LocationHandler<T> {

    override val config by lazy { config() }
    override suspend fun handle() {
        handlerData.forEach {
            it.value.handler?.let { it1 -> it1(config, this) }
        }
    }
    override fun <R> withBot(bot: Bot, block: BotContext<PollAnswerHandler>.() -> R): R {
        return LocationPollAnswerHandler(update, bot.service, bot.fileService, handlerData, locationsWrapper).block()
    }
}