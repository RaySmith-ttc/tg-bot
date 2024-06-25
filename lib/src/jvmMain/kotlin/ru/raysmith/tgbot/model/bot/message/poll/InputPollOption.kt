package ru.raysmith.tgbot.model.bot.message.poll

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.raysmith.tgbot.model.network.message.MessageEntity
import ru.raysmith.tgbot.model.network.message.ParseMode

/** This object contains information about one answer option in a poll to send. */
@Serializable
data class InputPollOption(

    /** Option text, 1-100 characters */
    @SerialName("text") val text: String,

    /**
     * Mode for parsing entities in the text. See [formatting options][ParseMode] for more details.
     * Currently, only custom emoji entities are allowed
     * */
    @SerialName("text_parse_mode") val textParseMode: ParseMode? = null,

    /**
     * A list of special entities that appear in the poll option text. It can be specified instead of [textParseMode]
     * */
    @SerialName("text_entities") val textEntities: List<MessageEntity>? = null,
)