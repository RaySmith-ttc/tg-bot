package ru.raysmith.tgbot.model.api.message.checklist

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.raysmith.tgbot.model.api.user.User
import ru.raysmith.tgbot.model.api.chat.Chat
import ru.raysmith.tgbot.model.api.message.MessageEntity

/** Describes a task in a checklist. */
@Serializable
data class ChecklistTask(

    /** Unique identifier of the task */
    @SerialName("id") val id: Int,

    /** Text of the task */
    @SerialName("text") val text: String,

    /** Special entities that appear in the task text */
    @SerialName("text_entities") val textEntities: List<MessageEntity>? = null,

    /** User that completed the task; omitted if the task wasn't completed by a user */
    @SerialName("completed_by_user") val completedByUser: User? = null,

    /** Chat that completed the task; omitted if the task wasn't completed by a chat */
    @SerialName("completed_by_chat") val completedByChat: Chat? = null,

    /** Point in time (Unix timestamp) when the task was completed; 0 if the task wasn't completed */
    @SerialName("completion_date") val completionDate: Int? = null,
)
