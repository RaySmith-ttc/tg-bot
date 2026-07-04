package ru.raysmith.tgbot.model.api.message

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.raysmith.tgbot.model.api.user.User

/** Describes a topic of a direct messages chat. */
@Serializable
data class DirectMessagesTopic(

    /** Unique identifier of the topic */
    @SerialName("topic_id") val topicId: Long,

    /** Information about the user that created the topic. Currently, it is always present. */
    @SerialName("user") val user: User? = null,
)
