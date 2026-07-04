package ru.raysmith.tgbot.model.api.message.service

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.raysmith.tgbot.model.api.message.Message

/** Describes a service message about the rejection of a suggested post. */
@Serializable
data class SuggestedPostDeclined(

    /**
     * Message containing the suggested post. Note that the [Message] object in this field will not contain the
     * [replyToMessage][Message.replyToMessage] field even if it itself is a reply.
     * */
    @SerialName("suggested_post_message") val suggestedPostMessage: Message? = null,

    /** Comment with which the post was declined */
    @SerialName("comment") val comment: String? = null,
)
