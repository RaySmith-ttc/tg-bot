package ru.raysmith.tgbot.model.api.poll

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.raysmith.tgbot.model.api.user.User
import ru.raysmith.tgbot.model.api.chat.Chat
import ru.raysmith.tgbot.model.api.message.MessageEntity

/** This object contains information about one answer option in a poll. */
@Serializable
data class PollOption(

    /** Unique identifier of the option, persistent on option addition and deletion */
    @SerialName("persistent_id") val persistentId: String,

    /** Option text, 1-100 characters */
    @SerialName("text") val text: String,

    /** Special entities that appear in the option text. Currently, only custom emoji entities are allowed in poll option texts */
    @SerialName("text_entities") val textEntities: List<MessageEntity>? = null,

    /** Media added to the poll option */
    @SerialName("media") val media: PollMedia? = null,

    /** Number of users that who voted for this option ; may be 0 if unknown */
    @SerialName("voter_count") val voterCount: Int,

    /** User who added the option; omitted if the option wasn't added by a user after poll creation */
    @SerialName("added_by_user") val addedByUser: User? = null,

    /** Chat that added the option; omitted if the option wasn't added by a chat after poll creation */
    @SerialName("added_by_chat") val addedByChat: Chat? = null,

    /** Point in time (Unix timestamp) when the option was added; omitted if the option existed in the original poll */
    @SerialName("addition_date") val additionDate: Int? = null
)
