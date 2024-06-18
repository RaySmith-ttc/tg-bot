package ru.raysmith.tgbot.model.network.message

import kotlinx.serialization.Serializable
import ru.raysmith.tgbot.model.network.chat.Chat
import ru.raysmith.tgbot.network.serializer.MaybeInaccessibleMessageSerializer

/**
 * This object describes a message that can be inaccessible to the bot. It can be one of
 *
 * - [Message]
 * - [InaccessibleMessage]
 * */
@Serializable(with = MaybeInaccessibleMessageSerializer::class)
sealed class MaybeInaccessibleMessage {
    /** Chat the message belonged to */
    abstract val chat: Chat

    /** Unique message identifier inside the chat */
    abstract val messageId: Int

    /** Always 0. The field can be used to differentiate regular and inaccessible messages. */
    abstract val date: Int
}