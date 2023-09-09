package ru.raysmith.tgbot.model.network.chat.forum

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/** This object represents a service message about an edited forum topic. */
@Serializable
data class ForumTopicEdited(

    /** New name of the topic, if it was edited */
    @SerialName("name") val name: String? = null,

    /** New identifier of the custom emoji shown as the topic icon, if it was edited; an empty string if the icon was removed */
    @SerialName("icon_custom_emoji_id") val iconCustomEmojiId: String? = null,
)