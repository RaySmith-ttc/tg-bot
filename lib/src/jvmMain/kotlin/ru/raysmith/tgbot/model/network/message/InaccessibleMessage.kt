package ru.raysmith.tgbot.model.network.message

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.raysmith.tgbot.model.network.chat.Chat

// fix MaybeInaccessibleMessageSerializer if changed

/** This object describes a message that was deleted or is otherwise inaccessible to the bot. */
@Serializable
data class InaccessibleMessage(

    /** Chat the message belonged to */
    @SerialName("chat") override val chat: Chat,

    /** Unique message identifier inside the chat */
    @SerialName("message_id") override val messageId: Int,

    /** Always 0. The field can be used to differentiate regular and inaccessible messages. */
    @SerialName("date") override val date: Int
) : MaybeInaccessibleMessage()