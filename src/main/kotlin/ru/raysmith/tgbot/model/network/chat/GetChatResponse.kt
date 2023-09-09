package ru.raysmith.tgbot.model.network.chat


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.raysmith.tgbot.model.network.chat.member.ChatMember

@Serializable
data class GetChatResponse(
    @SerialName("ok") val ok: Boolean,
    @SerialName("result") val result: Chat
)

@Serializable
data class ChatMemberResponse(
    @SerialName("ok") val ok: Boolean,
    @SerialName("result") val result: ChatMember
)

@Serializable
data class ChatMembersResponse(
    @SerialName("ok") val ok: Boolean,
    @SerialName("result") val result: List<ChatMember>
)