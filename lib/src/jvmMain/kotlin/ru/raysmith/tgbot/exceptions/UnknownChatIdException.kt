package ru.raysmith.tgbot.exceptions

// TODO [docs] describe possible causes
class UnknownChatIdException(override val message: String = "Unknown chat id") : BotException(message)