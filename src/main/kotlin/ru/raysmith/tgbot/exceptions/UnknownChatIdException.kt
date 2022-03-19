package ru.raysmith.tgbot.exceptions

import ru.raysmith.tgbot.exceptions.BotException

// TODO [docs] describe possible causes
class UnknownChatIdException(override val message: String = "Chat id unknown") : BotException(message)