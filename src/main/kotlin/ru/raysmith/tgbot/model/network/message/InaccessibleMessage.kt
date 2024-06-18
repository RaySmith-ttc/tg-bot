package ru.raysmith.tgbot.model.network.message

import kotlinx.serialization.Serializable
import ru.raysmith.tgbot.model.network.chat.Chat

/** This object describes a message that was deleted or is otherwise inaccessible to the bot. */
@Serializable
data class InaccessibleMessage(

    /** Chat the message belonged to */
    override val chat: Chat,

    /** Unique message identifier inside the chat */
    override val messageId: Int,

    /** Always 0. The field can be used to differentiate regular and inaccessible messages. */
    override val date: Int
) : MaybeInaccessibleMessage()