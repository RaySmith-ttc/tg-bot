package ru.raysmith.tgbot.model.network.chat.forum

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/** This object represents a service message about a new forum topic created in the chat. */
@Serializable
data class ForumTopicCreated(

    /** Name of the topic */
    @SerialName("name") val name: String,

    /** Color of the topic icon in RGB format */
    @SerialName("icon_color") val iconColor: Int,

    /** Unique identifier of the custom emoji shown as the topic icon */
    @SerialName("icon_custom_emoji_id") val iconCustomEmojiId: String? = null,
)