package ru.raysmith.tgbot.model.network.message

import kotlinx.serialization.Serializable

/** This object contains information about the quoted part of a message that is replied to by the given message. */
@Serializable
data class TextQuote(

    /** Text of the quoted part of a message that is replied to by the given message */
    val text: String,

    /**
     * Special entities that appear in the quote. Currently, only *bold*, *italic*, *underline*, *strikethrough*,
     * *spoiler*, and *custom_emoji* entities are kept in quotes.
     * */
    val entities: List<MessageEntity>? = null,

    /** Approximate quote position in the original message in UTF-16 code units as specified by the sender */
    val position: Int,

    /**
     * *True*, if the quote was chosen manually by the message sender.
     * Otherwise, the quote was added automatically by the server.
     * */
    val isManual: Boolean?

)
