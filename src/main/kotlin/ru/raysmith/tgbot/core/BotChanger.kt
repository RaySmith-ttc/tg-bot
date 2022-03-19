package ru.raysmith.tgbot.core

/** Allows to change a bot for the [handler][T] */
interface BotChanger<T : EventHandler> {

    /** Uses the [bot] token to make requests from [block] to telegram. */
    fun withBot(bot: Bot, block: T.() -> Any)
}