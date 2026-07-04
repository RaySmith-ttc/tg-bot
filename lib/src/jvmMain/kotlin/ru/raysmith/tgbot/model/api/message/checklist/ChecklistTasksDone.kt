package ru.raysmith.tgbot.model.api.message.checklist

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.raysmith.tgbot.model.api.message.Message

/** Describes a service message about checklist tasks marked as done or not done. */
@Serializable
data class ChecklistTasksDone(

    /**
     * Message containing the checklist whose tasks were marked as done or not done. Note that the [Message] object in
     * this field will not contain the [replyToMessage][Message.replyToMessage] field even if it itself is a reply.
     * */
    @SerialName("checklist_message") val checklistMessage: Message? = null,

    /** Identifiers of the tasks that were marked as done */
    @SerialName("marked_as_done_task_ids") val markedAsDoneTaskIds: List<Int>? = null,

    /** Identifiers of the tasks that were marked as not done */
    @SerialName("marked_as_not_done_task_ids") val markedAsNotDoneTaskIds: List<Int>? = null,
)
