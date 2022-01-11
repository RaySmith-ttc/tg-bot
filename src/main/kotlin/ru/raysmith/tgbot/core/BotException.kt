package ru.raysmith.tgbot.core

class BotException(override val message: String) : RuntimeException(message)