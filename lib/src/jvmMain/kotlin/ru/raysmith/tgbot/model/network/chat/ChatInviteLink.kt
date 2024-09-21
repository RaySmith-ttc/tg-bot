package ru.raysmith.tgbot.model.network.chat

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.raysmith.tgbot.model.network.User

/** Represents an invite link for a chat. */
@Serializable
data class ChatInviteLink(
    /**
     * The invite link. If the link was created by another chat administrator,
     * then the second part of the link will be replaced with “…”.
     * */
    @SerialName("invite_link") val inviteLink: String,

    /** Creator of the link */
    @SerialName("creator") val creator: User,

    /** *True*, if the link is primary */
    @SerialName("is_primary") val isPrimary: Boolean,

    /** *True*, if the link is revoked */
    @SerialName("is_revoked") val isRevoked: Boolean,

    /** Point in time (Unix timestamp) when the link will expire or has been expired */
    @SerialName("expire_date") val expireDate: Int? = null,

    /**
     * Maximum number of users that can be members of the chat simultaneously after joining the chat
     * via this invite link; 1-99999
     * */
    @SerialName("member_limit") val memberLimit: Int? = null,

    /** Number of pending join requests created using this link */
    @SerialName("pending_join_request_count") val pendingJoinRequestCount: Int? = null,

    /** The number of seconds the subscription will be active for before the next payment */
    @SerialName("subscription_period") val subscriptionPeriod: Int? = null,

    /**
     * The amount of Telegram Stars a user must pay initially and after each subsequent subscription period to be
     * a member of the chat using the link
     */
    @SerialName("subscription_price") val subscriptionPrice: Int? = null,
)