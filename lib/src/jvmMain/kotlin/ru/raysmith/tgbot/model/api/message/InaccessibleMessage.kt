package ru.raysmith.tgbot.model.api.message

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.raysmith.tgbot.model.api.chat.Chat

// fix MaybeInaccessibleMessageSerializer if changed

/** This object describes a message that was deleted or is otherwise inaccessible to the bot. */
@Serializable
data class InaccessibleMessage(

    /** Chat the message belonged to */
    @SerialName("chat") override val chat: Chat,

    /** Unique message identifier inside the chat */
    @SerialName("message_id") override val messageId: Int,
) : MaybeInaccessibleMessage
