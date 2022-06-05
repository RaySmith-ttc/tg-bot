package ru.raysmith.tgbot.core

import ru.raysmith.tgbot.exceptions.UnknownChatIdException

//// TODO use instead strings
//sealed class ChatId {
//    data class Id(val id: Long) : ChatId()
//
//    class ChannelUsername(username: String) : ChatId() {
//        val username: String = if (username.startsWith("@")) username else "@$username"
//    }
//
//    companion object {
//        fun fromId(id: Long) = Id(id)
//        fun fromChannelUsername(username: String) = ChannelUsername(username)
//    }
//}

interface ChatIdHolder {
    /** Returns chat id that the update came from or null */
    fun getChatId(): String?

    /**
     * Returns chat id that the update came from or throw [UnknownChatIdException]
     *
     * The chat id may be null in CallbackQueryHandler when a message is too old
     * */
    fun getChatIdOrThrow() = getChatId() ?: throw UnknownChatIdException()
}