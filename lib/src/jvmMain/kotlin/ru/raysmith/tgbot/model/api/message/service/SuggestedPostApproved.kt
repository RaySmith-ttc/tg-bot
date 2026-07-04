package ru.raysmith.tgbot.model.api.message.service

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.raysmith.tgbot.model.api.suggestedpost.SuggestedPostPrice
import ru.raysmith.tgbot.model.api.message.Message

/** Describes a service message about the approval of a suggested post. */
@Serializable
data class SuggestedPostApproved(

    /**
     * Message containing the suggested post. Note that the [Message] object in this field will not contain the
     * [replyToMessage][Message.replyToMessage] field even if it itself is a reply.
     * */
    @SerialName("suggested_post_message") val suggestedPostMessage: Message? = null,

    /** Amount paid for the post */
    @SerialName("price") val price: SuggestedPostPrice? = null,

    /** Date when the post will be published */
    @SerialName("send_date") val sendDate: Int,
)
