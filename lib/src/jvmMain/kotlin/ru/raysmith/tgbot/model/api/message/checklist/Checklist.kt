package ru.raysmith.tgbot.model.api.message.checklist

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.raysmith.tgbot.model.api.message.MessageEntity

/** Describes a checklist. */
@Serializable
data class Checklist(

    /** Title of the checklist */
    @SerialName("title") val title: String,

    /** Special entities that appear in the checklist title */
    @SerialName("title_entities") val titleEntities: List<MessageEntity>? = null,

    /** List of tasks in the checklist */
    @SerialName("tasks") val tasks: List<ChecklistTask>,

    /** *True*, if users other than the creator of the list can add tasks to the list */
    @SerialName("others_can_add_tasks") val othersCanAddTasks: Boolean? = null,

    /** *True*, if users other than the creator of the list can mark tasks as done or not done */
    @SerialName("others_can_mark_tasks_as_done") val othersCanMarkTasksAsDone: Boolean? = null,
)
