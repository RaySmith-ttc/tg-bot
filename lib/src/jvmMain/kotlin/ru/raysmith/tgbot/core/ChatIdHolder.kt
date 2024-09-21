package ru.raysmith.tgbot.core

import ru.raysmith.tgbot.exceptions.UnknownChatIdException
import ru.raysmith.tgbot.model.bot.ChatId

interface ChatIdHolder {
    /** Returns chat id that the update came from or null */
    fun getChatId(): ChatId?

    /**
     * Returns chat id that the update came from or throw [UnknownChatIdException]
     *
     * The chat id may be null in CallbackQueryHandler when a message is too old
     * */
    fun getChatIdOrThrow() = getChatId() ?: throw UnknownChatIdException()
}

