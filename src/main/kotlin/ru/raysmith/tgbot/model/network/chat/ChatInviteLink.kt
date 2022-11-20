package ru.raysmith.tgbot.model.network.chat

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.raysmith.tgbot.model.network.User

@Serializable
data class ChatInviteLinkResponse(
    @SerialName("ok") val ok: Boolean,
    @SerialName("result") val result: ChatInviteLink
)

@Serializable
/** Represents an invite link for a chat. */
data class ChatInviteLink(
    /**
     * The invite link. If the link was created by another chat administrator,
     * then the second part of the link will be replaced with “…”.
     * */
    @SerialName("invite_link") val inviteLink: String,

    /** Creator of the link */
    @SerialName("creator") val creator: User,

    /** True, if the link is primary */
    @SerialName("is_primary") val isPrimary: Boolean,

    /** True, if the link is revoked */
    @SerialName("is_revoked") val isRevoked: Boolean,

    /** Point in time (Unix timestamp) when the link will expire or has been expired */
    @SerialName("expire_date") val expireDate: Int? = null,

    /** Maximum number of users that can be members of the chat simultaneously after joining the chat via this invite link; 1-99999 */
    @SerialName("member_limit") val memberLimit: Int? = null,
)