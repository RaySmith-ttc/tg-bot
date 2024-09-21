package ru.raysmith.tgbot.model.network.chat.member

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.raysmith.tgbot.model.network.User
import ru.raysmith.tgbot.network.serializer.ChatMemberSerializer

@Serializable(with = ChatMemberSerializer::class)
sealed class ChatMember {

    /** The member's status in the chat */
    abstract val status: String

    /** Information about the user */
    abstract val user: User
}