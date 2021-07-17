package ru.raysmith.tgbot.model.network

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
/** This object represents a Telegram user or bot. */
data class User(

    /** Unique identifier for this user or bot. This number may have more than 32 significant bits and some
     * programming languages may have difficulty/silent defects in interpreting it. But it has at most 52
     * significant bits, so a 64-bit integer or double-precision float type are safe for storing this identifier.
     * */
    @SerialName("id") val id: Long,

    /** True, if this user is a bot */
    @SerialName("is_bot") val isBot: Boolean,

    /** User's or bot's first name */
    @SerialName("first_name") val firstName: String,

    /** User's or bot's last name */
    @SerialName("last_name") val lastName: String? = null,

    /** User's or bot's username */
    @SerialName("username") val username: String? = null,

    /**
     * IETF language tag of the user's language
     *
     * @see <a href="https://en.wikipedia.org/wiki/IETF_language_tag">IETF language tag</a>
     * */
    @SerialName("language_code") val languageCode: String? = null,

    /** True, if the bot can be invited to groups. Returned only in getMe. */ // TODO link all (getMe) to method
    @SerialName("can_join_groups") val canJoinGroups: Boolean? = null,

    /** True, if privacy mode is disabled for the bot. Returned only in getMe. */
    @SerialName("can_read_all_group_messages") val canReadAllGroupMessages: Boolean? = null,

    /** True, if the bot supports inline queries. Returned only in getMe. */
    @SerialName("supports_inline_queries") val supportsInlineQueries: Boolean? = null,
) {



}