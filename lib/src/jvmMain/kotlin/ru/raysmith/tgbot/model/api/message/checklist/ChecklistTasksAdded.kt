package ru.raysmith.tgbot.model.api.message.checklist

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.raysmith.tgbot.model.api.message.Message

/** Describes a service message about tasks added to a checklist. */
@Serializable
data class ChecklistTasksAdded(

    /**
     * Message containing the checklist to which the tasks were added. Note that the [Message] object in this field will
     * not contain the [replyToMessage][Message.replyToMessage] field even if it itself is a reply.
     * */
    @SerialName("checklist_message") val checklistMessage: Message? = null,

    /** List of tasks added to the checklist */
    @SerialName("tasks") val tasks: List<ChecklistTask>,
)
