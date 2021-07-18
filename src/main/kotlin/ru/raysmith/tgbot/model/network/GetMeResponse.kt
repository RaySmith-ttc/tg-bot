package ru.raysmith.tgbot.model.network

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GetMeResponse(
    @SerialName("ok")
    val ok: Boolean,
    @SerialName("result")
    val result: Result
)

@Serializable
data class Result(
    @SerialName("can_join_groups")
    val canJoinGroups: Boolean,
    @SerialName("can_read_all_group_messages")
    val canReadAllGroupMessages: Boolean,
    @SerialName("first_name")
    val firstName: String,
    @SerialName("id")
    val id: Int,
    @SerialName("is_bot")
    val isBot: Boolean,
    @SerialName("supports_inline_queries")
    val supportsInlineQueries: Boolean,
    @SerialName("username")
    val username: String
)