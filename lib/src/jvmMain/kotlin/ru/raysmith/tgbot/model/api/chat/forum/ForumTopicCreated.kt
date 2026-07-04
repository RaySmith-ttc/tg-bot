package ru.raysmith.tgbot.model.api.chat.forum

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

    /**
     * *True*, if the name of the topic wasn't specified explicitly by its creator and likely needs to be changed by the
     * bot
     * */
    @SerialName("is_name_implicit") val isNameImplicit: Boolean? = null,
)
