package ru.raysmith.tgbot.model.api.chat.member

import kotlinx.serialization.Serializable
import ru.raysmith.tgbot.model.api.user.User
import ru.raysmith.tgbot.model.serializer.ChatMemberSerializer

/**
 * This object contains information about one member of a chat. Currently, the following 6 types of chat members are
 * supported:
 *
 * - [ChatMemberOwner]
 * - [ChatMemberAdministrator]
 * - [ChatMemberMember]
 * - [ChatMemberRestricted]
 * - [ChatMemberLeft]
 * - [ChatMemberBanned]
 * */
@Serializable(ChatMemberSerializer::class)
sealed interface ChatMember {

    /** The member's status in the chat */
    val status: String

    /** Information about the user */
    val user: User
}
