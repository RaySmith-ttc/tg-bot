package ru.raysmith.tgbot.model.network.chat.forum

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/** This object represents a forum topic. */
@Serializable
data class ForumTopic(

    /** Unique identifier of the forum topic */
    @SerialName("message_thread_id") val messageThreadId: Int,

    /** Name of the topic */
    @SerialName("name") val name: String,

    /** Color of the topic icon in RGB format */
    @SerialName("icon_color") val iconColor: IconColor,

    /** Unique identifier of the custom emoji shown as the topic icon */
    @SerialName("icon_custom_emoji_id") val iconCustomEmojiId: String? = null,
)