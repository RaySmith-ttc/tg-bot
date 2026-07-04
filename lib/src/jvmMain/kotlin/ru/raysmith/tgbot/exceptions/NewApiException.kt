package ru.raysmith.tgbot.exceptions

import kotlinx.serialization.SerializationException

/**
 * This exception is thrown when the bot receives a response from the Telegram Bot API that is not compatible with the
 * current version of the library.
 * It indicates that the API has been updated and the library needs to be updated.
 * */
class NewApiException(message: String) : SerializationException(message)
