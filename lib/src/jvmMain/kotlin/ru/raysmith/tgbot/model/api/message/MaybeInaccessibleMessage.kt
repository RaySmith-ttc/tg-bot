package ru.raysmith.tgbot.model.api.message

import kotlinx.serialization.Serializable
import ru.raysmith.tgbot.model.api.chat.Chat
import ru.raysmith.tgbot.model.serializer.MaybeInaccessibleMessageSerializer

/**
 * This object describes a message that can be inaccessible to the bot. It can be one of
 *
 * - [Message]
 * - [InaccessibleMessage]
 * */
@Serializable(MaybeInaccessibleMessageSerializer::class)
sealed interface MaybeInaccessibleMessage {

    /** Chat the message belonged to */
    val chat: Chat

    /** Unique message identifier inside the chat */
    val messageId: Int

    fun asMessageOrNull() = this as? Message
}
