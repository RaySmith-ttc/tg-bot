package ru.raysmith.tgbot.model.api.chat.member

import kotlinx.serialization.Contextual
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.Required
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.raysmith.tgbot.model.api.user.User
import ru.raysmith.tgbot.model.serializer.ZonedDateTimeAsUnixSerializer
import java.time.ZonedDateTime

/** Represents a chat member that has no additional privileges or restrictions. */
@Serializable
data class ChatMemberMember(
    @SerialName("user") override val user: User,

    /** Tag of the member */
    @SerialName("tag") val tag: String? = null,

    /** Date when the user's subscription will expire */
    @SerialName("until_date") @Serializable(ZonedDateTimeAsUnixSerializer::class) val untilDate: ZonedDateTime? = null,
) : ChatMember {

    /** The member's status in the chat, always “member” */
    @Required
    @EncodeDefault
    @SerialName("status")
    override val status: String = "member"
}
