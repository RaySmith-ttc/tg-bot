package ru.raysmith.tgbot.model.api.message

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/** This object contains information about the quoted part of a message that is replied to by the given message. */
@Serializable
data class TextQuote(

    /** Text of the quoted part of a message that is replied to by the given message */
    @SerialName("text") val text: String,

    /**
     * Special entities that appear in the quote. Currently, only *bold*, *italic*, *underline*, *strikethrough*,
     * *spoiler*, *custom_emoji* and *date_time* entities are kept in quotes.
     * */
    @SerialName("entities") val entities: List<MessageEntity>? = null,

    /** Approximate quote position in the original message in UTF-16 code units as specified by the sender */
    @SerialName("position") val position: Int,

    /**
     * *True*, if the quote was chosen manually by the message sender.
     * Otherwise, the quote was added automatically by the server.
     * */
    @SerialName("is_manual") val isManual: Boolean? = null,
)
