package ru.raysmith.tgbot.model.network.chat


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GetChatResponse(
    @SerialName("ok") val ok: Boolean,
    @SerialName("result") val result: Result
)

@Serializable
data class Result(
    @SerialName("all_members_are_administrators") val allMembersAreAdministrators: Boolean? = null,
    @SerialName("id") val id: Int,
    @SerialName("invite_link") val inviteLink: String? = null,
    @SerialName("permissions") val permissions: Permissions? = null,
    @SerialName("title") val title: String? = null,
    @SerialName("type") val type: ChatType
)

