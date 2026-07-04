package ru.raysmith.tgbot.model.api.chat.member

import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.Required
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.raysmith.tgbot.model.api.user.User
import ru.raysmith.tgbot.model.serializer.ZonedDateTimeAsUnixSerializer
import java.time.ZonedDateTime

/** Represents a chat member that was banned in the chat and can't return to the chat or view chat messages. */
@Serializable
data class ChatMemberBanned(
    @SerialName("user") override val user: User,

    /**
     * Date when restrictions will be lifted for this user. If [isRestrictedForever] returns true, then the user is
     * banned forever.
     * */
    @SerialName("until_date") @Serializable(ZonedDateTimeAsUnixSerializer::class) val untilDate: ZonedDateTime,
) : ChatMember {

    /** The member's status in the chat, always “kicked” */
    @Required
    @EncodeDefault
    @SerialName("status")
    override val status: String = "kicked"

    fun isRestrictedForever() = untilDate.toEpochSecond() == 0L
}
