package ru.raysmith.tgbot.model.api.poll

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.raysmith.tgbot.model.api.message.MaybeInaccessibleMessage
import ru.raysmith.tgbot.model.api.message.Message
import ru.raysmith.tgbot.model.api.message.MessageEntity

/** Describes a service message about an option added to a poll. */
@Serializable
data class PollOptionAdded(

    /**
     * Message containing the poll to which the option was added, if known. Note that the [Message] object in this field
     * will not contain the [replyToMessage][Message.replyToMessage] field even if it itself is a reply.
     * */
    @SerialName("poll_message") val pollMessage: MaybeInaccessibleMessage? = null,

    /** Unique identifier of the added option */
    @SerialName("option_persistent_id") val optionPersistentId: String,

    /** Option text */
    @SerialName("option_text") val optionText: String,

    /** Special entities that appear in the [optionText] */
    @SerialName("option_text_entities") val optionTextEntities: List<MessageEntity>? = null,
)
