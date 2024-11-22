package ru.raysmith.tgbot

/**
 * This object represents a chat.
 */
external interface WebAppChat {

    /** Unique identifier for this chat. */
    val id: Long

    /** Type of chat, can be either [WebAppChatType.group], [WebAppChatType.supergroup] or [WebAppChatType.channel]. */
    val type: String

    /** Title of the chat. */
    val title: String

    /** Username of the chat. */
    val username: String?

    /** URL of the chat’s photo. The photo can be in .jpeg or .svg formats. */
    @JsName("photo_url")
    val photoUrl: String?
}