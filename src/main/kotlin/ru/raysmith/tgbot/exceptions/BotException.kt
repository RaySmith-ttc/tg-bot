package ru.raysmith.tgbot.exceptions

open class BotException(override val message: String) : RuntimeException(message)
