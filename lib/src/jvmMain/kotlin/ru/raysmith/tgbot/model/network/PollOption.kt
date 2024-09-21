package ru.raysmith.tgbot.model.network

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.raysmith.tgbot.model.network.message.MessageEntity

/** This object contains information about one answer option in a poll. */
@Serializable
data class PollOption(

    /** Option text, 1-100 characters */
    @SerialName("text") val text: String,

    /**
     * Special entities that appear in the option text.
     * Currently, only custom emoji entities are allowed in poll option texts
     * */
    @SerialName("text_entities") val textEntities: List<MessageEntity>? = null,

    /** Number of users that voted for this option */
    @SerialName("voter_count") val voterCount: String,
)