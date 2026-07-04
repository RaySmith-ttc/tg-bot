package ru.raysmith.tgbot.model.api.business

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.raysmith.tgbot.model.api.user.User

/** Describes the connection of the bot with a business account. */
@Serializable
data class BusinessConnection(

    /** Unique identifier of the business connection */
    @SerialName("id") val id: String,

    /** Business account user that created the business connection */
    @SerialName("user") val user: User,

    /** Identifier of a private chat with the user who created the business connection. */
    @SerialName("user_chat_id") val userChatId: Long,

    /** Date the connection was established in Unix time. */
    @SerialName("date") val date: Int,

    /** Rights of the business bot */
    @SerialName("rights") val rights: BusinessBotRights,

    /** *True*, if the connection is active */
    @SerialName("is_enabled")  val isEnabled: Boolean

)
