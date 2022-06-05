package ru.raysmith.tgbot.core

import ru.raysmith.tgbot.network.TelegramService

/** Allows to change a bot for the [handler][T] */
interface BotContext<T : EventHandler> : ChatIdHolder {

    val service: TelegramService

    /** Uses the [bot] token to make requests to telegram from [block]. */
    fun withBot(bot: Bot, block: BotContext<T>.() -> Any)
}