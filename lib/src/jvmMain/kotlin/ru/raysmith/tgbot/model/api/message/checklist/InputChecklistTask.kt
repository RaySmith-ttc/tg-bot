package ru.raysmith.tgbot.model.api.message.checklist

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.raysmith.tgbot.model.api.message.MessageEntity
import ru.raysmith.tgbot.model.api.message.ParseMode

/** Describes a task to add to a checklist. */
@Serializable
data class InputChecklistTask(

    /**
     * Unique identifier of the task;
     * must be positive and unique among all task identifiers currently present in the checklist
     * */
    @SerialName("id") val id: Int,

    /** Text of the task; 1-100 characters after entities parsing */
    @SerialName("text") val text: String,

    /** Mode for parsing entities in the text. See [formatting options][ParseMode] for more details. */
    @SerialName("parse_mode") val parseMode: ParseMode? = null,

    /**
     * List of special entities that appear in the text, which can be specified instead of parse_mode.
     * Currently, only *bold*, *italic*, *underline*, *strikethrough*, *spoiler*, *custom_emoji*, and *date_time*
     * entities are allowed.
     * */
    @SerialName("text_entities") val textEntities: List<MessageEntity>? = null,
)
