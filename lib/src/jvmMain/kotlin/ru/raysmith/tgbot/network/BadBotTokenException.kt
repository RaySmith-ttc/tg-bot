package ru.raysmith.tgbot.network

import ru.raysmith.tgbot.exceptions.BotException

class BadBotTokenException(override val message: String) : BotException(message)