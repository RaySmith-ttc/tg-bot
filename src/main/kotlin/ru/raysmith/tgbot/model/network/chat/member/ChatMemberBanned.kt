package ru.raysmith.tgbot.model.network.chat.member

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.raysmith.tgbot.model.network.User

/** Represents a chat member that was banned in the chat and can't return to the chat or view chat messages. */
@Serializable
data class ChatMemberBanned(
    /** The member's status in the chat, always “kicked” */
    @SerialName("status") override val status: String,
    @SerialName("user") override val user: User,

    /** Date when restrictions will be lifted for this user; unix time. If 0, then the user is banned forever */
    @SerialName("until_date") val untilDate: Int,
) : ChatMember()