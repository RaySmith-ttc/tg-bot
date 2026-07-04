package ru.raysmith.tgbot.model.api.message.checklist

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.raysmith.tgbot.model.api.message.MessageEntity
import ru.raysmith.tgbot.model.api.message.ParseMode

/** Describes a checklist to create. */
@Serializable
data class InputChecklist(

    /** Title of the checklist; 1-255 characters after entities parsing */
    @SerialName("title") val title: String,

    /** Mode for parsing entities in the title. See [formatting options][ParseMode] for more details. */
    @SerialName("parse_mode") val parseMode: String? = null,

    /**
     * List of special entities that appear in the title, which can be specified instead of parse_mode.
     * Currently, only *bold*, *italic*, *underline*, *strikethrough*, *spoiler*, *custom_emoji*, and *date_time*
     * entities are allowed.
     * */
    @SerialName("title_entities") val titleEntities: List<MessageEntity>? = null,

    /** List of 1-30 tasks in the checklist */
    @SerialName("tasks") val tasks: List<InputChecklistTask>,

    /** Pass *True* if other users can add tasks to the checklist */
    @SerialName("others_can_add_tasks") val othersCanAddTasks: Boolean? = null,

    /** Pass *True* if other users can mark tasks as done or not done in the checklist */
    @SerialName("others_can_mark_tasks_as_done") val othersCanMarkTasksAsDone: Boolean? = null,
)
